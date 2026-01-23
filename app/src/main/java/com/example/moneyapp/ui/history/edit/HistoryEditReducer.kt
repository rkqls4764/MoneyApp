package com.example.moneyapp.ui.history.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.TransactionType
import com.example.moneyapp.data.entity.TransactionWithCategory
import com.example.moneyapp.ui.history.add.HistoryField
import java.time.LocalDateTime

object HistoryEditReducer {
    fun reduce(s: HistoryEditState, e: HistoryEditEvent): HistoryEditState = when (e) {
        is HistoryEditEvent.InitWith -> handleInit(e.data)
        is HistoryEditEvent.ChangedValueWith -> handleChangedValue(s, e.field, e.value)
        is HistoryEditEvent.ChangedTypeWith -> handleChangedType(s, e.type)
        is HistoryEditEvent.ChangedDateWith -> handleChangedDate(s, e.date)
        is HistoryEditEvent.ChangedCategoryWith -> handleChangedCategory(s, e.category)
        else -> s
    }

    private fun handleInit(
        data: TransactionWithCategory
    ): HistoryEditState {
        return HistoryEditState(inputData = data, selectedCategoryName = data.category?.name ?: "")
    }

    private val historyUpdaters: Map<HistoryField, (TransactionWithCategory, String) -> TransactionWithCategory> =
        mapOf(
            HistoryField.NAME        to { s, v -> s.copy(transaction = s.transaction.copy(description = v)) },
            HistoryField.AMOUNT      to { s, v -> s.copy(transaction = s.transaction.copy(amount = v.toLongOrNull() ?: 0L)) },
            HistoryField.MEMO        to { s, v -> s.copy(transaction = s.transaction.copy(memo = v)) }
        )

    private fun handleChangedValue(
        state: HistoryEditState,
        field: HistoryField,
        value: String
    ): HistoryEditState {
        val updater = historyUpdaters[field] ?: return state
        return state.copy(inputData = updater(state.inputData, value))
    }

    private fun handleChangedType(
        state: HistoryEditState,
        type: TransactionType
    ): HistoryEditState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(type = type, categoryId = null)), selectedCategoryName = "")
    }

    private fun handleChangedDate(
        state: HistoryEditState,
        date: LocalDateTime
    ): HistoryEditState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(date = date)))
    }

    private fun handleChangedCategory(
        state: HistoryEditState,
        category: Category
    ): HistoryEditState {
        return state.copy(inputData = state.inputData.copy(transaction = state.inputData.transaction.copy(categoryId = category.id)), selectedCategoryName = category.name)
    }
}