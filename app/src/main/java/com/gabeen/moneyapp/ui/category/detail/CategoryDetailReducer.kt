package com.gabeen.moneyapp.ui.category.detail

import com.gabeen.moneyapp.data.entity.Category

object CategoryDetailReducer {
    fun reduce(s: CategoryDetailState, e: CategoryDetailEvent): CategoryDetailState = when (e) {
        is CategoryDetailEvent.InitWith -> handleInit(e.categoryInfo)
        else -> s
    }

    private fun handleInit(
        categoryInfo: Category
    ): CategoryDetailState {
        return CategoryDetailState(categoryInfo = categoryInfo)
    }
}