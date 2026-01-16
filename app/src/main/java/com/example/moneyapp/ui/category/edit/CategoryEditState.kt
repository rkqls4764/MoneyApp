package com.example.moneyapp.ui.category.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType

data class CategoryEditState(
    val inputData: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)