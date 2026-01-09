package com.example.moneyapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.util.Date

@Dao
interface MoneyDao {
    // ===============================================================
    // 1. 쓰기 작업 (데이터 변경)
    // ==============================================================

    // 내역 추가
    // suspend : 스레드에서 동작(db 작업은 오래걸리므로 다른 곳에서 실행)
    // onConflict = OnConflictStrategy.REPLACE : id가 이미 있는 경우 새로운 걸로 덮어쓰기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: MoneyTransaction)

    // 내역 수정
    @Update
    suspend fun update(transaction: MoneyTransaction)

    // 내역 삭제
    @Delete
    suspend fun delete(transaction: MoneyTransaction)


    // ===============================================================
    // 2. 읽기 작업 (데이터 조회)
    // ===============================================================

    // 캘린더 화면용 (월별 조회)
    @Transaction // @Relation 쓰면 트랜잭션 필수
    @Query("SELECT * FROM transaction_table " +
            "WHERE date BETWEEN :startDate and :endDate " +
            "ORDER BY date DESC")
    fun getTransactionsForCalendar(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionWithCategory>>


    // 내역 목록 조회 (검색 조건)
    // - 검색 조건 : 기간 / 지출.수입 / 카테고리 / 키워드(내역 이름, 메모)
    // - 검색 조건 설정 안하면 전체 목록 조회
    @Transaction
    @Query("""
        SELECT * FROM transaction_table
        WHERE (:startDate IS NULL OR date >= :startDate)
        AND (:endDate IS NULL OR date <= :endDate)
        AND (:useTypeFilter = 0 OR type IN (:types))
        AND (:useCategoryFilter = 0 OR categoryId IN (:categoryIds))
        AND (:keyword IS NULL OR description LIKE '%' || :keyword || '%' OR memo LIKE '%' || :keyword || '%')
        ORDER BY date DESC
    """)
    fun searchTransactions(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        types: List<TransactionType>,   // 다중 선택 가능 , IN 연산자는 null 들어오면 오류남
        useTypeFilter: Boolean,         // 타입이 선택됐는지 (false인 경우 전체 조회)
        categoryIds: List<Long>,        // 다중 선택 가능
        useCategoryFilter: Boolean,     // 카테고리가 선택됐는지 (false인 경우 전체 조회)
        keyword: String?
    ): Flow<List<TransactionWithCategory>>


    // 검색 결과 통계용 (기간+카테고리별 지출/수입/총계)

    // 추후 통계 기능 추가










}