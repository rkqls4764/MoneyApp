package com.example.moneyapp.ui.history.detail

import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import java.time.LocalDateTime

data class HistoryDetailState(
    val historyInfo: TransactionWithCategory = TransactionWithCategory(
        transaction = MoneyTransaction(
            id = 0,
            amount = 0,
            description = "",
            memo = "",
            type = TransactionType.INCOME,
            categoryId = null,
            date = LocalDateTime.now()
        ),
        category = null
    )
)