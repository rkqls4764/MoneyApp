package com.gabeen.moneyapp.ui.history.detail

import com.gabeen.moneyapp.data.entity.TransactionWithCategory

object HistoryDetailReducer {
    fun reduce(s: HistoryDetailState, e: HistoryDetailEvent): HistoryDetailState = when (e) {
        is HistoryDetailEvent.InitWith -> handleInit(e.historyInfo)
        else -> s
    }

    private fun handleInit(
        historyInfo: TransactionWithCategory
    ): HistoryDetailState {
        return HistoryDetailState(historyInfo = historyInfo)
    }
}