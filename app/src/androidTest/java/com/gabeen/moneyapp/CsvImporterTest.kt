package com.gabeen.moneyapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gabeen.moneyapp.data.database.AppDatabase
import com.gabeen.moneyapp.data.database.CategoryDao
import com.gabeen.moneyapp.data.database.MoneyDao
import com.gabeen.moneyapp.data.util.CsvImporter
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
public class CsvImporterTest {
    private lateinit var db: AppDatabase
    private lateinit var transactionDao: MoneyDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var importer: CsvImporter

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // 메모리에만 존재하는 임시 DB 생성
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        transactionDao = db.moneyDao()
        categoryDao = db.categoryDao()
        importer = CsvImporter(transactionDao, categoryDao) // DAO를 주입받는 구조라고 가정
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun testCsvImportAndCategoryReuse() = runBlocking {
        // 1. 가짜 CSV 데이터 준비 (내용 동일, 카테고리 "식비" 중복)
        val csvContent = """
            사용자,거래일,수입/지출,금액,분류,하위 분류,내역,지불,카드,메모
            나,2025-07-28,지출,10000,식비,,점심식사,,,맛있음
            나,2025-07-29,지출,5000,식비,,커피,,,써요
        """.trimIndent()

        val inputStream = csvContent.byteInputStream()

        // 2. 임포트 실행
        importer.processCsv(inputStream)

        // 3. 검증: 트랜잭션이 2개 생성되었는가?
        val allTransactions = transactionDao.getAllTransactionsForTest() // 테스트용 전체조회 쿼리 필요

        println("======= [Transaction 리스트 출력] =======")
        allTransactions.forEachIndexed { index, tx ->
            println("""
                        [Item ${index + 1}]
                        ID: ${tx.id}
                        금액: ${tx.amount}
                        내역: ${tx.description}
                        타입: ${tx.type}
                        카테고리 ID: ${tx.categoryId}
                        날짜: ${tx.date}
                        메모: ${tx.memo ?: "없음"}
                        ---------------------------------------
                    """.trimIndent())
        }

        assertEquals(2, allTransactions.size)

        // 4. 검증: 카테고리 "식비"가 하나만 생성되었는가? (중복 생성 방지 확인)
        val allCategories = categoryDao.getAllCategoriesForTest()

        println("======= [Category 리스트 출력] =======")
        allCategories.forEach { category ->
            println("ID: ${category.id} | 이름: ${category.name} | 타입: ${category.type}")
        }

        assertEquals(1, allCategories.size)
        assertEquals("식비", allCategories[0].name)

        // 5. 검증: 두 트랜잭션의 categoryId가 동일한가?
        assertEquals(allCategories[0].id, allTransactions[0].categoryId)
        assertEquals(allCategories[0].id, allTransactions[1].categoryId)
    }
}
