package com.example.moneyapp.di

import android.content.Context
import androidx.room.Room
import com.example.moneyapp.data.database.AppDatabase
import com.example.moneyapp.data.database.MoneyDao
import com.example.moneyapp.data.repository.MoneyRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RoomModule {
    // Database
    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "money_database"
        )
            .fallbackToDestructiveMigration() // 개발 중에만 사용 (데이터 날리기)
            .build()
    }

    // DAO
    @Provides
//    @Singleton    // DAO는 @Singloton 붙이나 안붙이나 똑같대 삭제해주면 될듯
    fun provideMoneyDao(database: AppDatabase): MoneyDao {
        return database.moneyDao()
    }

    // Repository
//    @Provides     // 그리고 Repository는 모듈에 추가 안해줘도돼!!
//    @Singleton
//    fun provideMoneyRepository(moneyDao: MoneyDao): MoneyRepository {
//        return MoneyRepository(moneyDao)
//    }
}