package com.example.moneyapp.ui.history.add

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import java.time.LocalDateTime

object HistoryAddReducer {
    fun reduce(s: HistoryAddState, e: HistoryAddEvent): HistoryAddState = when (e) {
        HistoryAddEvent.Init -> HistoryAddState()
        is HistoryAddEvent.ChangedValueWith -> handleChangedValue(s, e.field, e.value)
        is HistoryAddEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        is HistoryAddEvent.ChangedDateWith -> handleChangedDate(s, e.date)
        is HistoryAddEvent.ChangedCategoryWith -> handleChangedCategory(s, e.category)
        else -> s
    }

    private val historyUpdaters: Map<HistoryField, (TransactionWithCategory, String) -> TransactionWithCategory> =
        mapOf(
            HistoryField.NAME        to { s, v -> s.copy(transaction = s.transaction.copy(description = v)) },
            HistoryField.AMOUNT      to { s, v -> s.copy(transaction = s.transaction.copy(amount = v.toLong())) },
            HistoryField.MEMO        to { s, v -> s.copy(transaction = s.transaction.copy(memo = v)) }
        )

    private fun handleChangedValue(
        state: HistoryAddState,
        field: HistoryField,
        value: String
    ): HistoryAddState {
        val updater = historyUpdaters[field] ?: return state
        return state.copy(inputData = updater(state.inputData, value))
    }

    private fun handleChangedType(
        state: HistoryAddState,
        type: TransactionType
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(type = type, categoryId = null)), selectedCategoryName = "")
    }

    private fun handleChangedDate(
        state: HistoryAddState,
        date: LocalDateTime
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(date = date)))
    }

    private fun handleChangedCategory(
        state: HistoryAddState,
        category: Category
    ): HistoryAddState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(categoryId = category.id)), selectedCategoryName = category.name)
    }
}