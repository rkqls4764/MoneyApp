package com.gabeen.moneyapp

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gabeen.moneyapp.data.database.AppDatabase
import com.gabeen.moneyapp.data.database.CategoryDao
import com.gabeen.moneyapp.data.database.MoneyDao
import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.MoneyTransaction
import com.gabeen.moneyapp.data.entity.TransactionType
import com.gabeen.moneyapp.data.repository.MoneyRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDateTime

@RunWith(AndroidJUnit4::class)
class MoneyRepositoryTest {

    private lateinit var db: AppDatabase
    private lateinit var moneyDao: MoneyDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var repository: MoneyRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        // 1. 메모리에만 존재하는 가짜 DB 생성 (테스트 끝나면 삭제됨)
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()

        moneyDao = db.moneyDao()
        categoryDao = db.categoryDao()

        // 2. Repository에 가짜 DAO 주입
        repository = MoneyRepository(moneyDao)
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
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
            date = LocalDateTime.now(), // 오늘 날짜
        )

        // 2. When: Repository를 통해 데이터 저장 (insert)
        repository.insert(transaction)

        // 3. Then: 캘린더 데이터 가져오기 함수 호출해서 확인
        // flow.first()는 흐르는 데이터 중 가장 첫 번째(최신) 값을 가져옵니다.
        // 시작일/종료일은 넉넉하게 잡아서 조회
        // 시작 시간 (0에 해당): 1970년 1월 1일 0시 0분
        val start = LocalDateTime.of(1970, 1, 1, 0, 0)

        // 끝 시간 (System.currentTimeMillis()에 해당): 현재 시간
        val end = LocalDateTime.now()

        // 호출
        val results = repository.getCalendarData(start, end).first()

        // 검사: 저장한 개수가 1개여야 하고, 내용이 "테스트 커피"여야 한다.
        assertEquals(1, results.size)
        assertEquals("테스트 커피", results[0].transaction.memo)
        assertEquals(5000L, results[0].transaction.amount)

        println("✅ 테스트 성공! 데이터가 정상적으로 저장되고 조회되었습니다.")
    }

    @Test
    fun searchByCategory_returnsOnlySelectedCategory() = runBlocking {
        // [준비 1] 카테고리 2개 만들기
        val foodCategory = Category(name = "식비", type = TransactionType.EXPENSE)
        val trafficCategory = Category(name = "교통비", type = TransactionType.EXPENSE)

        // Room이 ID를 자동생성하므로, insert 후 ID를 알아내야 함 (이 부분은 DAO 구현에 따라 다를 수 있음)
        categoryDao.insert(foodCategory)
        categoryDao.insert(trafficCategory)

        // DB에 들어간 진짜 객체 꺼내오기 (ID를 알기 위해)
        val categories = categoryDao.getAllCategories().first()
        val foodId = categories.find { it.name == "식비" }!!.id
        val trafficId = categories.find { it.name == "교통비" }!!.id

        // [준비 2] 내역 만들기 (외래키 연결)
        val t1 = MoneyTransaction(
            amount = 10000,
            description = "점심",
            date = LocalDateTime.now(),
            type = TransactionType.EXPENSE,
            categoryId = foodId.toLong(), // 식비 연결
            memo = null
        )
        val t2 = MoneyTransaction(
            amount = 5000,
            description = "택시",
            date = LocalDateTime.now(),
            type = TransactionType.EXPENSE,
            categoryId = trafficId.toLong(), // 교통비 연결
            memo = null
        )

        moneyDao.insert(t1)
        moneyDao.insert(t2)

        // [실행] "식비"만 검색해줘!
        val result = repository.search(
            startDate = null,
            endDate = null,
            types = null,        // 타입 필터 안 함
            categoryIds = listOf(foodId.toLong()), // 👈 식비 ID만 필터링!
            keyword = null
        ).first() // Flow에서 데이터 한 번 꺼내기

        // [검증]
        assertEquals(1, result.size) // 1개만 나와야 함
        assertEquals("점심", result[0].transaction.description) // 그게 "점심"이어야 함
        assertEquals("식비", result[0].category?.name) // 같이 딸려온 카테고리 이름 확인
    }

    // 🔥 추가: 다중 필터 테스트 (지출 + 키워드)
    @Test
    fun searchByTypeAndKeyword_returnsCorrectData() = runBlocking {
        // 데이터 준비 (카테고리 없이)
        moneyDao.insert(MoneyTransaction(amount = 100, description = "편의점 간식", date = LocalDateTime.now(), type = TransactionType.EXPENSE, memo = null, categoryId = null))
        moneyDao.insert(MoneyTransaction(amount = 200, description = "월급", date = LocalDateTime.now(), type = TransactionType.INCOME, memo = null, categoryId = null))
        moneyDao.insert(MoneyTransaction(amount = 300, description = "편의점 커피", date = LocalDateTime.now(), type = TransactionType.EXPENSE, memo = null, categoryId = null))

        // [실행] "지출"이면서 "편의점" 들어간 거 찾아줘
        val result = repository.search(
            startDate = null,
            endDate = null,
            types = listOf(TransactionType.EXPENSE), // 지출만
            categoryIds = null,
            keyword = "편의점" // 키워드
        ).first()

        // [검증]
        assertEquals(2, result.size) // 간식, 커피 2개 나와야 함
        assertTrue(result.all { it.transaction.type == TransactionType.EXPENSE }) // 모두 지출이어야 함
    }

    // 수정(Update) 테스트
    @Test
    fun updateTransaction_changesData() = runBlocking {
        // [Given] 초기 데이터 저장
        val originalTransaction = MoneyTransaction(
            amount = 1000,
            description = "수정전",
            memo = "메모전",
            type = TransactionType.EXPENSE,
            categoryId = null,
            date = LocalDateTime.now()
        )
        repository.insert(originalTransaction)

        // 중요: Insert 된 데이터를 DB에서 다시 꺼내와야 함 (자동 생성된 ID를 알기 위해)
        val start = LocalDateTime.of(1970, 1, 1, 0, 0)
        val end = LocalDateTime.now()
        val savedList = repository.getCalendarData(start, end).first()
        val savedTransaction = savedList[0].transaction // ID가 세팅된 진짜 객체

        // [When] 데이터 수정 (금액 1000 -> 9999, 내용 "수정전" -> "수정후")
        // copy를 사용하면 ID는 그대로 유지되고 나머지 값만 바뀝니다.
        val updatedTransaction = savedTransaction.copy(
            amount = 9999,
            description = "수정후"
        )
        repository.update(updatedTransaction)

        // [Then] 다시 조회해서 바뀐 값인지 확인
        val resultList = repository.getCalendarData(start, end).first()
        val resultTransaction = resultList[0].transaction

        assertEquals(1, resultList.size) // 개수는 여전히 1개여야 함
        assertEquals(9999L, resultTransaction.amount) // 금액이 바뀌었는지
        assertEquals("수정후", resultTransaction.description) // 내용이 바뀌었는지
        assertEquals("메모전", resultTransaction.memo) // 안 바꾼 건 그대로인지
    }

    // 삭제(Delete) 테스트
    @Test
    fun deleteTransaction_removesData() = runBlocking {
        // [Given] 데이터 저장
        val transaction = MoneyTransaction(
            amount = 5000,
            description = "삭제할거",
            memo = null,
            type = TransactionType.EXPENSE,
            categoryId = null,
            date = LocalDateTime.now()
        )
        repository.insert(transaction)

        // 저장 확인 (ID 확보를 위해 조회)
        val start = LocalDateTime.of(1970, 1, 1, 0, 0)
        val end = LocalDateTime.now()
        val savedList = repository.getCalendarData(start, end).first()

        // 데이터가 1개 들어갔는지 먼저 확인
        assertEquals(1, savedList.size)
        val itemToDelete = savedList[0].transaction

        // [When] 삭제 수행
        repository.delete(itemToDelete)

        // [Then] 다시 조회했을 때 리스트가 비어있어야 함
        val resultList = repository.getCalendarData(start, end).first()

        assertTrue(resultList.isEmpty()) // 리스트가 비었는지 확인 (size == 0)
        assertEquals(0, resultList.size)
    }

    // 통계(집계) 기능 테스트
    @Test
    fun getCategoryStats_calculatesCorrectSums() = runBlocking {
        // [Given 1] 카테고리 3개 준비 (식비, 교통비, 월급)
        val foodCat = Category(name = "식비", type = TransactionType.EXPENSE)
        val trafficCat = Category(name = "교통비", type = TransactionType.EXPENSE)
        val salaryCat = Category(name = "월급", type = TransactionType.INCOME)

        categoryDao.insert(foodCat)
        categoryDao.insert(trafficCat)
        categoryDao.insert(salaryCat)

        // DB에서 ID 따오기
        val cats = categoryDao.getAllCategories().first()
        /* !! : non-null 이라는 의미. null이면 에러 출력해라 */
        val foodId = cats.find { it.name == "식비" }!!.id
        val trafficId = cats.find { it.name == "교통비" }!!.id
        val salaryId = cats.find { it.name == "월급" }!!.id

        // [Given 2] 내역 4개 추가 (같은 카테고리 여러 개 섞어서)
        val now = LocalDateTime.now() // 오늘

        // 식비 2건 (1000원 + 2000원 = 3000원 예상)
        moneyDao.insert(MoneyTransaction(amount = 1000, description = "김밥", categoryId = foodId.toLong(), type = TransactionType.EXPENSE, date = now, memo = ""))
        moneyDao.insert(MoneyTransaction(amount = 2000, description = "라면", categoryId = foodId.toLong(), type = TransactionType.EXPENSE, date = now, memo = ""))

        // 교통비 1건 (5000원 예상)
        moneyDao.insert(MoneyTransaction(amount = 5000, description = "택시", categoryId = trafficId.toLong(), type = TransactionType.EXPENSE, date = now, memo = ""))

        // 월급 1건 (10000원 예상)
        moneyDao.insert(MoneyTransaction(amount = 10000, description = "급여", categoryId = salaryId.toLong(), type = TransactionType.INCOME, date = now, memo = ""))

        // [When] 이번 달 통계 요청
        // 시작일: 1970년, 종료일: 내일 (모든 데이터 포함되게 넉넉히)
        val start = LocalDateTime.of(1970, 1, 1, 0, 0)
        val end = now.plusDays(1)

        /* 보통 flow 타입을 반환하기 때문에 first()로 끊어줌 */
        val stats = repository.getCategoryStats(start, end).first()

        // [Then] 검증 시작!

        // 1. 총 카테고리 3개가 나와야 함
        assertEquals(3, stats.size)

        // 2. 식비 검증
        val foodStat = stats.find { it.categoryId == 1L }
        /* ?. : null이면 null 리턴. null 아니면 . 뒤에꺼 꺼냄 */
        assertEquals(3000L, foodStat?.totalAmount)
        assertEquals(TransactionType.EXPENSE, foodStat?.type)

        // 3. 교통비 검증
        val trafficStat = stats.find { it.categoryName == "교통비" }
        assertEquals(5000L, trafficStat?.totalAmount)

        // 4. 월급 검증
        val salaryStat = stats.find { it.categoryName == "월급" }
        assertEquals(10000L, salaryStat?.totalAmount)
        assertEquals(TransactionType.INCOME, salaryStat?.type)
    }

}