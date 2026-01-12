package com.example.moneyapp.ui.history.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import java.time.LocalDateTime

data class HistoryEditState(
    val inputData: MoneyTransaction = MoneyTransaction(
        amount = 0,
        description = "",
        memo = "",
        type = TransactionType.EXPENSE,
        categoryId = null,
        date = LocalDateTime.now()
    ),
    val categories: List<Category> = listOf(
        Category(
            name = "식비",
            type = TransactionType.EXPENSE
        ),
        Category(
            name = "교통비",
            type = TransactionType.EXPENSE
        ),
        Category(
            name = "취미",
            type = TransactionType.EXPENSE
        ),
        Category(
            name = "기타",
            type = TransactionType.EXPENSE
        ),
        Category(
            name = "월급",
            type = TransactionType.INCOME
        ),
    ),
//    val categories: List<Category> = emptyList(),
    val selectedCategoryName: String = ""
)