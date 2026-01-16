package com.example.moneyapp.ui.category.detail

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType

data class CategoryDetailState(
    val categoryInfo: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)