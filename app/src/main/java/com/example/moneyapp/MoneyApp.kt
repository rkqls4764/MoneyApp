package com.example.moneyapp

import android.app.Application
import com.example.moneyapp.data.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MoneyApp : Application() {
    val database by lazy { AppDatabase.getDatabase(this) }
}