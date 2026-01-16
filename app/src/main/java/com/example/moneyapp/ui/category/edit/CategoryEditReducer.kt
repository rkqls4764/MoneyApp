package com.example.moneyapp.ui.category.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType

object CategoryEditReducer {
    fun reduce(s: CategoryEditState, e: CategoryEditEvent): CategoryEditState = when (e) {
        is CategoryEditEvent.InitWith -> handleInit(e.data)
        is CategoryEditEvent.ChangedNameWith -> handleChangedName(s, e.name)
        is CategoryEditEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        else -> s
    }

    private fun handleInit(
        data: Category
    ): CategoryEditState {
        return CategoryEditState(inputData = data)
    }

    private fun handleChangedName(
        state: CategoryEditState,
        value: String
    ): CategoryEditState {
        return state.copy(inputData = state.inputData.copy(name = value))
    }

    private fun handleChangedType(
        state: CategoryEditState,
        value: TransactionType
    ): CategoryEditState {
        return state.copy(inputData = state.inputData.copy(type = value))
    }
}