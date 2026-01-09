package com.example.moneyapp.data.repository

import com.example.moneyapp.data.database.MoneyDao
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject

// Repository에는 hilt 사용하면 @Inject constructor 붙여줘야해
class MoneyRepository @Inject constructor(private val moneyDao: MoneyDao) {
    // ===============================================================
    // 1. 쓰기 작업 (데이터 변경)
    // ===============================================================

    suspend fun insert(transaction: MoneyTransaction) {
        moneyDao.insertTransaction(transaction)
    }

    suspend fun update(transaction: MoneyTransaction) {
        moneyDao.updateTransaction(transaction)
    }

    suspend fun delete(transaction: MoneyTransaction) {
        moneyDao.deleteTransaction(transaction)
    }

    // ===============================================================
    // 2. 읽기 작업 (데이터 조회)
    // ===============================================================

    // 캘린더용
    fun getCalendarData(startDate: LocalDate, endDate: LocalDate): Flow<List<MoneyTransaction>> {
        val start = startDate.atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        val end = endDate.plusDays(1)
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant().toEpochMilli() - 1

        return moneyDao.getTransactionsForCalendar(
            startDate = start,
            endDate = end
        )
    }


    // 검색용
    // 검색조건 : 날짜(기간), 수입/지출, 카테고리, 검색어(메모, 내역 이름)
    // 검색조건 없을 시 전체 목록 조회
    fun search(
        startDate: Long?,
        endDate: Long?,
        type: TransactionType?,
        // category: Category?,
        keyword: String?
    ): Flow<List<MoneyTransaction>> {
        return moneyDao.searchTransactions(startDate, endDate, type, keyword)
    }

    // 통계용
    // 수입/지출/잔액 조회
    // 검색조건 : 날짜(기간), 수입/지출, 카테고리








}