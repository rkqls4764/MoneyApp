package com.example.moneyapp.ui.history.edit

import com.example.moneyapp.data.entity.Category
import com.example.moneyapp.data.entity.MoneyTransaction
import com.example.moneyapp.data.entity.TransactionType
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
        data: MoneyTransaction
    ): HistoryEditState {
        return HistoryEditState(inputData = data)
    }

    private val historyUpdaters: Map<HistoryField, (MoneyTransaction, String) -> MoneyTransaction> =
        mapOf(
            HistoryField.NAME        to { s, v -> s.copy(description = v) },
            HistoryField.AMOUNT      to { s, v -> s.copy(amount = v.toLong()) },
            HistoryField.MEMO        to { s, v -> s.copy(memo = v) }
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
        return state.copy(inputData = state.inputData.copy(type = type))
    }

    private fun handleChangedDate(
        state: HistoryEditState,
        date: LocalDateTime
    ): HistoryEditState {
        return state.copy(inputData = state.inputData.copy(date = date))
    }

    private fun handleChangedCategory(
        state: HistoryEditState,
        category: Category
    ): HistoryEditState {
        return state.copy(inputData = state.inputData.copy(categoryId = category.id), selectedCategoryName = category.name)
    }
}