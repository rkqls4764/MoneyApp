package com.example.moneyapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category): Long

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    // 전체 조회
    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<Category>>

    // 파일 import 할 때 필요한거
    // 카테고리 있는지 확인
    @Query("SELECT id FROM category_table WHERE name = :name AND type = :type LIMIT 1")
    suspend fun getCategoryIdByNameAndType(name: String, type: TransactionType): Long?

    // 테스트 검증용: 모든 카테고리 가져오기
    @Query("SELECT * FROM category_table")
    suspend fun getAllCategoriesForTest(): List<Category>
}