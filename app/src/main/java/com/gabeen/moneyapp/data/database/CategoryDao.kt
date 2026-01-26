package com.gabeen.moneyapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.gabeen.moneyapp.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Delete
    suspend fun delete(category: Category)

    // 전체 조회
    @Query("SELECT * FROM category_table")
    fun getAllCategories(): Flow<List<Category>>
}