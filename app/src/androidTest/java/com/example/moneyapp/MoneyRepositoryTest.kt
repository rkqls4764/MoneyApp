package com.example.moneyapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.moneyapp.data.database.AppDatabase
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.repository.MoneyRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class MoneyRepositoryTest {

    private lateinit var database: AppDatabase
    private lateinit var repository: MoneyRepository

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        database = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ).build()

        repository = MoneyRepository(database.moneyDao())
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveTransaction() = runBlocking {
        // Given: 테스트용 데이터 만들기
        val transaction = MoneyTransaction(
            amount = 5000,
            memo = "테스트 커피",
            description = "스타벅스",
            type = TransactionType.EXPENSE,
            categoryId = null,
            date = Date() // 오늘 날짜
        )

        // 2. When: Repository를 통해 데이터 저장 (insert)
        repository.insert(transaction)

        // 3. Then: 캘린더 데이터 가져오기 함수 호출해서 확인
        // flow.first()는 흐르는 데이터 중 가장 첫 번째(최신) 값을 가져옵니다.
        // 시작일/종료일은 넉넉하게 잡아서 조회
        val results = repository.getCalendarData(0, System.currentTimeMillis()).first()

        // 검사: 저장한 개수가 1개여야 하고, 내용이 "테스트 커피"여야 한다.
        assertEquals(1, results.size)
        assertEquals("테스트 커피", results[0].memo)
        assertEquals(5000L, results[0].amount)

        println("✅ 테스트 성공! 데이터가 정상적으로 저장되고 조회되었습니다.")
    }
}