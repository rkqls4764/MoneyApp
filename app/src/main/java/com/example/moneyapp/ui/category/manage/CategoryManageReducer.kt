package com.example.moneyapp.ui.category.manage

import com.example.moneyapp.data.entity.TransactionType

object CategoryManageReducer {
    fun reduce(s: CategoryManageState, e: CategoryManageEvent): CategoryManageState = when (e) {
        is CategoryManageEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        else -> s
    }

    private fun handleChangedType(
        state: CategoryManageState,
        value: TransactionType
    ): CategoryManageState {
        return state.copy(type = value)
    }
}