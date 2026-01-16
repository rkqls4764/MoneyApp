package com.example.moneyapp.ui.category.add

import com.example.moneyapp.data.entity.TransactionType

object CategoryAddReducer {
    fun reduce(s: CategoryAddState, e: CategoryAddEvent): CategoryAddState = when (e) {
        CategoryAddEvent.Init -> CategoryAddState()
        is CategoryAddEvent.ChangedNameWith -> handleChangedName(s, e.name)
        is CategoryAddEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        else -> s
    }

    private fun handleChangedName(
        state: CategoryAddState,
        value: String
    ): CategoryAddState {
        return state.copy(inputData = state.inputData.copy(name = value))
    }

    private fun handleChangedType(
        state: CategoryAddState,
        value: TransactionType
    ): CategoryAddState {
        return state.copy(inputData = state.inputData.copy(type = value))
    }
}