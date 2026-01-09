package com.example.moneyapp.data.repository

import com.example.moneyapp.data.database.CategoryDao
import com.example.moneyapp.data.entity.Category
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) {
    // 카테고리 생성
    suspend fun insert(category: Category) {
        categoryDao.insert(category)
    }

    // 카테고리 수정
    suspend fun update(category: Category) {
        categoryDao.update(category)
    }

    // 카테고리 삭제
    suspend fun delete(category: Category) {
        categoryDao.delete(category)
    }

    // 카테고리 전체 조회
    fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories()
    }
}