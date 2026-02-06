package com.gabeen.moneyapp.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.gabeen.moneyapp.data.database.CategoryDao
import com.gabeen.moneyapp.data.database.MoneyDao
import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.MoneyTransaction
import com.gabeen.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.InputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CsvManager @Inject constructor(
    private val moneyDao: MoneyDao,
    private val categoryDao: CategoryDao
){
    /**
     * @param uri 사용자가 선택한 파일의 '주소'
     */
    suspend fun importCsv(uri: Uri, context: Context) {
        val inputStream = context.contentResolver.openInputStream(uri)

        inputStream?.use { stream ->
            processImport(stream)
        }
    }

    // UI나 테스트에서 호출할 메서드
    suspend fun processImport(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val categoryCache = mutableMapOf<String, Long>()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        withContext(Dispatchers.IO) {
            try {
                // 1. 헤더(첫 줄)를 읽어서 이름 -> 인덱스 맵 생성
                val headerLine = reader.readLine() ?: return@withContext
                val headers = headerLine.split(",").map { it.trim() }
                val headerMap = headers.withIndex().associate { it.value to it.index }

                // 안전하게 인덱스를 가져오기 위한 헬퍼 함수
                fun getIdx(name: String) = headerMap[name] ?: -1

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    // 2. 콤마로 분리 (데이터가 비어있을 수 있으므로 -1 설정)
                    val tokens = line!!.split(",", limit = 10)
                    if (tokens.size < 10) continue

                    // 필수 인덱스 매핑: [거래일, 수입/지출, 금액, 분류, 내역, 메모]
                    val dateStr = tokens.getOrNull(getIdx("거래일"))?.trim() ?: ""
                    val typeStr = tokens.getOrNull(getIdx("수입/지출"))?.trim() ?: ""
                    val amountStr = tokens.getOrNull(getIdx("금액"))?.trim() ?: "0"
                    val categoryName = tokens.getOrNull(getIdx("분류"))?.trim() ?: "분류없음"
                    val description = tokens.getOrNull(getIdx("내역"))?.trim() ?: ""
                    val memo = tokens.getOrNull(getIdx("메모"))?.trim() ?: ""

                    if (dateStr.isEmpty() || amountStr.isEmpty()) continue

                    // 3. 데이터 변환 및 가공
                    val transType = if (typeStr == "수입") TransactionType.INCOME else TransactionType.EXPENSE
                    val amount = amountStr.replace("[^0-9]".toRegex(), "").toLongOrNull() ?: 0L

                    // 날짜 변환 (2025-07-28 -> LocalDateTime)
                    val date = LocalDate.parse(dateStr, dateFormatter).atStartOfDay()

                    // 4. 카테고리 처리 (Find or Create)
                    val cacheKey = "${categoryName}_${transType}"
                    var categoryId = categoryCache[cacheKey]

                    if (categoryId == null) {
                        categoryId = categoryDao.getCategoryIdByNameAndType(categoryName, transType)
                        if (categoryId == null) {
                            val newCategory = Category(name = categoryName, type = transType)
                            categoryId = categoryDao.insert(newCategory)
                        }
                        categoryCache[cacheKey] = categoryId!!
                    }

                    // 5. DB 저장
                    val transaction = MoneyTransaction(
                        amount = amount,
                        description = description,
                        memo = memo.ifEmpty { null },
                        type = transType,
                        categoryId = categoryId,
                        date = date
                    )
                    moneyDao.insert(transaction)
                }
            } catch (e: Exception) {
                Log.e("CsvManager-import", "CSV 파싱 중 에러 발생", e)
                throw e // 테스트에서 확인 가능하도록 던짐
            } finally {
                reader.close()
            }
        }
    }

    // --- Export 기능 ---

    /**
     * @param startDate 시작일 (00:00:00)
     * @param endDate 종료일 (23:59:59)
     */
    suspend fun exportCsv(uri: Uri, context: Context, startDate: LocalDateTime, endDate: LocalDateTime) {
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        // 1. 기간에 해당하는 데이터를 카테고리 이름과 함께 가져옴 (JOIN 쿼리 결과)
        val transactions = moneyDao.getAllTransactionsForExport(startDate, endDate)

        withContext(Dispatchers.IO) {
            try {
                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    val writer = BufferedWriter(OutputStreamWriter(outputStream, "UTF-8"))

                    // 2. CSV 헤더 작성
                    writer.write("거래일,수입/지출,금액,분류,내역,메모")
                    writer.newLine()

                    // 3. 데이터 행 작성
                    transactions.forEach { tx ->
                        val row = StringBuilder().apply {
                            append(tx.date.format(dateFormatter)) // 거래일
                            append("${if (tx.type == TransactionType.INCOME) "수입" else "지출"},") // 수입/지출
                            append("${tx.amount},") // 금액
                            append("${escapeCsv(tx.categoryName ?: "분류없음")},") // 분류. null일 경우 '분류없음'
                            append("${escapeCsv(tx.description)},") // 내역
                            append("${escapeCsv(tx.memo ?: "")},") // 메모
                        }.toString()

                        writer.write(row)
                        writer.newLine()
                    }
                    writer.flush()
                }
            } catch (e: Exception) {
                Log.e("CsvManager-export", "CSV 내보내기 중 에러 발생", e)
                throw e
            }
        }
    }

    /**
     * 텍스트에 콤마(,)나 줄바꿈이 포함된 경우 CSV 형식이 깨지지 않도록 큰따옴표로 감싸줌
     */
    private fun escapeCsv(value: String): String {
        return if (value.contains(",") || value.contains("\n") || value.contains("\"")) {
            "\"${value.replace("\"", "\"\"")}\""
        } else {
            value
        }
    }
}