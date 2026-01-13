package com.example.moneyapp.ui.history.detail

import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import java.time.LocalDateTime

data class HistoryDetailState(
    val historyInfo: MoneyTransaction = MoneyTransaction(
        amount = 100000,
        description = "택시",
        memo = "택시를 탔음",
        type = TransactionType.EXPENSE,
        categoryId = null,
        date = LocalDateTime.now()
    )
)