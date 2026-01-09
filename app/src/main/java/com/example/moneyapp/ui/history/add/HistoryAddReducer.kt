package com.example.moneyapp.ui.history.add

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import java.util.Date

object HistoryAddReducer {
    fun reduce(s: HistoryAddState, e: HistoryAddEvent): HistoryAddState = when (e) {
        is HistoryAddEvent.ChangedValueWith -> handleChangedValue(s, e.field, e.value)
        is HistoryAddEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        is HistoryAddEvent.ChangedDateWith -> handleChangedDate(s, e.date)
        is HistoryAddEvent.ChangedCategoryWith -> handleChangedCategory(s, e.category)
        else -> s
    }

    private val historyUpdaters: Map<HistoryAddField, (MoneyTransaction, String) -> MoneyTransaction> =
        mapOf(
            HistoryAddField.NAME        to { s, v -> s.copy(description = v) },
            HistoryAddField.AMOUNT      to { s, v -> s.copy(amount = v.toLong()) },
            HistoryAddField.MEMO        to { s, v -> s.copy(memo = v) }
        )

    private fun handleChangedValue(
        state: HistoryAddState,
        field: HistoryAddField,
        value: String
    ): HistoryAddState {
        val updater = historyUpdaters[field] ?: return state
        return state.copy(inputData = updater(state.inputData, value))
    }

    private fun handleChangedType(
        state: HistoryAddState,
        type: TransactionType
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(type = type))
    }

    private fun handleChangedDate(
        state: HistoryAddState,
        date: Date
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(date = date))
    }

    private fun handleChangedCategory(
        state: HistoryAddState,
        category: Category
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(categoryId = category.id), selectedCategoryName = category.name)
    }
}