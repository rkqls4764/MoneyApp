package com.gabeen.moneyapp.ui.category.add

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.TransactionType

data class CategoryAddState(
    val inputData: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)