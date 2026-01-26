package com.gabeen.moneyapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.MoneyTransaction

@Database(
    entities = [MoneyTransaction::class, Category::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun moneyDao(): MoneyDao
    abstract fun categoryDao(): CategoryDao

    // companion object == static
    companion object {
        // @Volatile - 변수 값을 캐시가 아닌 메인 메모리(RAM)에 바로 저장 : 최신값 읽어오기
        @Volatile
        private var INSTANCE: AppDatabase? = null // 처음 앱이 켜졌을 때 DB = null

        // Context - 안드로이드 시스템에 접근
        // ?. - 앞이 null이면 null 리턴, 아니라면 뒤에꺼 리턴
        // ?: - 앞이 null이면 뒤에꺼 리턴, 아니라면 앞에꺼 리턴 (어떤 경우에도 null을 리턴하지 않음)
        // synchronized(this) - DB를 두 번 생성하는 사고를 막기 위함
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                    AppDatabase::class.java,
                    "money_database"
                    )
                    .fallbackToDestructiveMigration() // 테이블 변경시 데이터 전부 삭제 후 새로 만듦 (개발 시에만 사용)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}