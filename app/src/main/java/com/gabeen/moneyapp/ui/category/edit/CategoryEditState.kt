package com.gabeen.moneyapp.ui.category.edit

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.TransactionType

data class CategoryEditState(
    val inputData: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)