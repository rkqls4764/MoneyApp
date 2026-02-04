package com.gabeen.moneyapp.data.util

import android.content.Context
import android.net.Uri
import android.util.Log
import com.example.moneyapp.data.database.CategoryDao
import com.example.moneyapp.data.database.MoneyDao
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class CsvImporter @Inject constructor(
    private val moneyDao: MoneyDao,
    private val categoryDao: CategoryDao
){
    /**
     * @param uri 사용자가 선택한 파일의 '주소'
     */
    suspend fun importCsv(uri: Uri, context: Context) {
        val inputStream = context.contentResolver.openInputStream(uri)

        inputStream?.use { stream ->
            processCsv(stream)
        }
    }

    // UI나 테스트에서 호출할 메서드
    suspend fun processCsv(inputStream: InputStream) {
        val reader = BufferedReader(InputStreamReader(inputStream, "UTF-8"))
        val categoryCache = mutableMapOf<String, Long>()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        withContext(Dispatchers.IO) {
            try {
                reader.readLine() // 1. 헤더 건너뛰기

                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    // 2. 콤마로 분리 (데이터가 비어있을 수 있으므로 -1 설정)
                    val tokens = line!!.split(",", limit = 10)
                    if (tokens.size < 10) continue

                    // 인덱스 매핑: [사용자(0), 거래일(1), 수입/지출(2), 금액(3), 분류(4), ... 내역(6), ... 메모(9)]
                    val dateStr = tokens[1].trim()
                    val typeStr = tokens[2].trim()
                    val amountStr = tokens[3].trim()
                    val categoryName = tokens[4].trim()
                    val description = tokens[6].trim()
                    val memo = tokens[9].trim()

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
                Log.e("CsvImporter", "CSV 파싱 중 에러 발생", e)
                throw e // 테스트에서 확인 가능하도록 던짐
            } finally {
                reader.close()
            }
        }
    }
}