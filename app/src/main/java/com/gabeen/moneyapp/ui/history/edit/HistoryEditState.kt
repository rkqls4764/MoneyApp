package com.gabeen.moneyapp.ui.history.edit

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.MoneyTransaction
import com.gabeen.moneyapp.data.entity.TransactionType
import com.gabeen.moneyapp.data.entity.TransactionWithCategory
import java.time.LocalDateTime

data class HistoryEditState(
    val inputData: TransactionWithCategory = TransactionWithCategory(
        transaction = MoneyTransaction(
            id = 0,
            amount = 0,
            description = "",
            memo = "",
            type = TransactionType.EXPENSE,
            categoryId = null,
            date = LocalDateTime.now()
        ),
        category = null
    ),
    val categories: List<Category> = emptyList()
)