package com.example.moneyapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface MoneyDao {
    // ===============================================================
    // 1. 쓰기 작업 (데이터 변경)
    // ==============================================================

    // 내역 추가
    // suspend : 스레드에서 동작(db 작업은 오래걸리므로 다른 곳에서 실행)
    // onConflict = OnConflictStrategy.REPLACE : id가 이미 있는 경우 새로운 걸로 덮어쓰기
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: MoneyTransaction)

    // 내역 수정
    @Update
    suspend fun updateTransaction(transaction: MoneyTransaction)

    // 내역 삭제
    @Delete
    suspend fun deleteTransaction(transaction: MoneyTransaction)


    // ===============================================================
    // 2. 읽기 작업 (데이터 조회)
    // ===============================================================

    // 캘린더 화면용 (월별 조회)
    @Query("SELECT * FROM transaction_table " +
            "WHERE date BETWEEN :startDate and :endDate " +
            "ORDER BY date DESC")
    fun getTransactionsForCalendar(startDate: Long, endDate: Long): Flow<List<MoneyTransaction>>


    // 내역 목록 조회 (검색 조건)
    // - 검색 조건 : 기간 / 지출.수입 / 카테고리 / 키워드(내역 이름, 메모)
    // - 검색 조건 설정 안하면 전체 목록 조회

    // 추후 카테고리 검색 조건 추가

    @Query("""
        SELECT * FROM transaction_table
        WHERE (:startDate IS NULL OR date >= :startDate)
        AND (:endDate IS NULL OR date <= :endDate)
        AND (:type IS NULL OR type = :type)
        AND (:keyword IS NULL OR description LIKE '%' || :keyword || '%' OR memo LIKE '%' || :keyword || '%')
        ORDER BY date DESC
    """)
    fun searchTransactions(
        startDate: Long?,
        endDate: Long?,
        type: TransactionType?,
        // category: Category?,
        keyword: String?
    ): Flow<List<MoneyTransaction>>


    // 검색 결과 통계용 (기간+카테고리별 지출/수입/총계)

    // 추후 통계 기능 추가










}