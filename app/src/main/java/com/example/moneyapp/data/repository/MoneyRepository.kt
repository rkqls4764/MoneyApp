package com.example.moneyapp.data.repository

import com.example.moneyapp.data.database.MoneyDao
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

class MoneyRepository(private val moneyDao: MoneyDao) {
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
    fun getCalendarData(startDate: Long, endDate: Long): Flow<List<MoneyTransaction>> {
        return moneyDao.getTransactionsForCalendar(startDate, endDate)
    }

    // 검색용
    // 검색조건 : 날짜(기간), 수입/지출, 카테고리, 검색어(메모, 내역 이름)
    // 검색조건 없을 시 전체 목록 조회
    fun search(
        startDate: Long?,
        endDate: Long?,
        type: TransactionType?,
        category: Category?,
        keyword: String?
    ): Flow<List<MoneyTransaction>> {
        return moneyDao.searchTransactions(startDate, endDate, type, category, keyword)
    }

    // 통계용
    // 수입/지출/잔액 조회
    // 검색조건 : 날짜(기간), 수입/지출, 카테고리








}