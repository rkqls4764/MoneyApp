package com.gabeen.moneyapp.ui.history.detail

import com.gabeen.moneyapp.data.entity.MoneyTransaction
import com.gabeen.moneyapp.data.entity.TransactionType
import com.gabeen.moneyapp.data.entity.TransactionWithCategory
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