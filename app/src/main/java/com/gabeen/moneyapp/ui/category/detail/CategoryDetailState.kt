package com.gabeen.moneyapp.ui.category.detail

import com.gabeen.moneyapp.data.entity.Category
import com.gabeen.moneyapp.data.entity.TransactionType

data class CategoryDetailState(
    val categoryInfo: Category = Category(
        id = 0,
        name = "",
        type = TransactionType.EXPENSE
    )
)