package com.gabeen.moneyapp.data.repository

import com.gabeen.moneyapp.data.database.MoneyDao
import com.gabeen.moneyapp.data.entity.CategoryStat
import com.gabeen.moneyapp.data.entity.MoneyTransaction
import com.gabeen.moneyapp.data.entity.TransactionType
import com.gabeen.moneyapp.data.entity.TransactionWithCategory
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import javax.inject.Inject

// hilt 사용시 Repository에 @Inject constructor 붙여야함
class MoneyRepository @Inject constructor(private val moneyDao: MoneyDao) {
    companion object {
        const val CATEGORY_UNCATEGORIZED = -1L
    }

    // ===============================================================
    // 1. 쓰기 작업 (데이터 변경)
    // ===============================================================

    suspend fun insert(transaction: MoneyTransaction) {
        moneyDao.insert(transaction)
    }

    suspend fun update(transaction: MoneyTransaction) {
        moneyDao.update(transaction)
    }

    suspend fun delete(transaction: MoneyTransaction) {
        moneyDao.delete(transaction)
    }

    // ===============================================================
    // 2. 읽기 작업 (데이터 조회)
    // ===============================================================

    // 캘린더용
    fun getCalendarData(startDate: LocalDateTime, endDate: LocalDateTime): Flow<List<TransactionWithCategory>> {
        return moneyDao.getTransactionsForCalendar(
            startDate = startDate,
            endDate = endDate
        )
    }


    // 검색용
    // 검색조건 : 날짜(기간), 수입/지출, 카테고리, 검색어(메모, 내역 이름)
    // 검색조건 없을 시 전체 목록 조회
    // '분류없음'의 경우 -1L 아이디로 임의 설정
    fun search(
        startDate: LocalDateTime?,
        endDate: LocalDateTime?,
        types: List<TransactionType>?,
        categoryIds:  List<Long>?,
        keyword: String?
    ): Flow<List<TransactionWithCategory>> {

        // 타입(수입/지출) 필터 설정
        val safeTypes = types ?: emptyList() // null이면 빈 리스트로
        val useTypeFilter = safeTypes.isNotEmpty() // null이면 false, 있으면 true

        // 카테고리 리스트 설정
        val rawCategoryIds = categoryIds ?: emptyList() // null이면 빈 리스트로
        val useCategoryFilter = rawCategoryIds.isNotEmpty() // null이면 false, 있으면 true
        val includeNullCategory = rawCategoryIds.contains(CATEGORY_UNCATEGORIZED) // 분류 없음 카테고리(0번 아이디) 포함 여부
        val realCategoryIds = rawCategoryIds.filter { it != CATEGORY_UNCATEGORIZED } // 0번 아이디 제외(없는 값임)

        return moneyDao.searchTransactions(
            startDate = startDate,
            endDate = endDate,
            types = safeTypes,
            useTypeFilter = useTypeFilter,
            categoryIds = realCategoryIds,
            useCategoryFilter = useCategoryFilter,
            includeNullCategory = includeNullCategory,
            keyword = keyword)
    }

    // 통계용
    // 수입/지출/잔액 조회
    // 검색조건 : 날짜(기간)

    // 결과 : 수입/지출 - 카테고리 별 총 금액
    fun getCategoryStats(start: LocalDateTime, end: LocalDateTime): Flow<List<CategoryStat>> {
        return moneyDao.getCategoryStats(start, end)
    }










}