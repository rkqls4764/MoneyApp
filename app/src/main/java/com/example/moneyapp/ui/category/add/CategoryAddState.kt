package com.example.moneyapp.ui.category.add

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType

data class CategoryAddState(
    val inputData: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)