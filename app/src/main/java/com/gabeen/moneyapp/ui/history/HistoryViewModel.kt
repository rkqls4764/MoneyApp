package com.gabeen.moneyapp.ui.history

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabeen.moneyapp.data.repository.CategoryRepository
import com.gabeen.moneyapp.data.repository.MoneyRepository
import com.gabeen.moneyapp.ui.effect.UiEffect
import com.gabeen.moneyapp.ui.history.add.HistoryAddEvent
import com.gabeen.moneyapp.ui.history.add.HistoryAddReducer
import com.gabeen.moneyapp.ui.history.add.HistoryAddState
import com.gabeen.moneyapp.ui.history.detail.HistoryDetailEvent
import com.gabeen.moneyapp.ui.history.detail.HistoryDetailReducer
import com.gabeen.moneyapp.ui.history.detail.HistoryDetailState
import com.gabeen.moneyapp.ui.history.edit.HistoryEditEvent
import com.gabeen.moneyapp.ui.history.edit.HistoryEditReducer
import com.gabeen.moneyapp.ui.history.edit.HistoryEditState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class HistoryTarget {
    ADD, EDIT
}

@HiltViewModel
class HistoryViewModel @Inject constructor(private val moneyRepository: MoneyRepository, private val categoryRepository: CategoryRepository) : ViewModel() {
    companion object {
        private const val TAG = "HistoryViewModel"
    }

    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val _historyAddState = MutableStateFlow(HistoryAddState())
    val historyAddState = _historyAddState.asStateFlow()
    private val _historyDetailState = MutableStateFlow(HistoryDetailState())
    val historyDetailState = _historyDetailState.asStateFlow()
    private val _historyEditState = MutableStateFlow(HistoryEditState())
    val historyEditState = _historyEditState.asStateFlow()

    fun onAddEvent(e: HistoryAddEvent) {
        _historyAddState.update { HistoryAddReducer.reduce(it, e) }

        when (e) {
            HistoryAddEvent.InitFirst -> getAllCategories(HistoryTarget.ADD)
            HistoryAddEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            HistoryAddEvent.ClickedAdd -> addHistory()
            else -> Unit
        }
    }

    fun onDetailEvent(e: HistoryDetailEvent) {
        _historyDetailState.update { HistoryDetailReducer.reduce(it, e) }

        when (e) {
            HistoryDetailEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            HistoryDetailEvent.ClickedEdit -> _uiEffect.tryEmit(UiEffect.Navigate("historyEdit"))
            HistoryDetailEvent.ClickedDelete -> deleteHistory()
            else -> Unit
        }
    }

    fun onEditEvent(e: HistoryEditEvent) {
        _historyEditState.update { HistoryEditReducer.reduce(it, e) }

        when (e) {
            is HistoryEditEvent.InitWith -> getAllCategories(HistoryTarget.EDIT)
            HistoryEditEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            HistoryEditEvent.ClickedUpdate -> updateHistory()
            else -> Unit
        }
    }

    /* 내역 추가 */
    fun addHistory() {
        viewModelScope.launch {
            val amount = historyAddState.value.inputData.transaction.amount
            if (amount <= 0) {
                _uiEffect.emit(UiEffect.ShowToast("금액을 1원 이상 입력해주세요"))
                return@launch
            }

            moneyRepository.insert(
                transaction = historyAddState.value.inputData.transaction
            )

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("내역이 추가되었습니다"))

            Log.d(TAG, "[addHistory] 내역 추가 성공\n${historyAddState.value.inputData.transaction}")
        }
    }

    /* 내역 수정 */
    fun updateHistory() {
        viewModelScope.launch {
            val amount = historyEditState.value.inputData.transaction.amount
            if (amount <= 0) {
                _uiEffect.emit(UiEffect.ShowToast("금액을 1원 이상 입력해주세요"))
                return@launch
            }

            moneyRepository.update(
                transaction = historyEditState.value.inputData.transaction
            )

            _historyDetailState.update { it.copy(historyInfo = historyEditState.value.inputData) }

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("내역이 수정되었습니다"))

            Log.d(TAG, "[updateHistory] 내역 수정 성공\n${historyEditState.value.inputData}")
            Log.d(TAG, "[updateHistory] \n${historyDetailState.value.historyInfo}")
        }
    }

    /* 내역 삭제 */
    fun deleteHistory() {
        viewModelScope.launch {
            moneyRepository.delete(
                transaction = historyDetailState.value.historyInfo.transaction
            )

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("내역이 삭제되었습니다"))

            Log.d(TAG, "[deleteHistory] 내역 삭제 성공\n${historyDetailState.value.historyInfo.transaction}")
        }
    }

    /* 카테고리 전체 조회 */
    fun getAllCategories(target: HistoryTarget) {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { data ->
                when (target) {
                    HistoryTarget.ADD -> _historyAddState.update { it.copy(categories = data) }
                    HistoryTarget.EDIT -> _historyEditState.update { it.copy(categories = data) }
                }

                Log.d(TAG, "[getAllCategories-${target}] 카테고리 전체 조회 성공\n${data}")
            }
        }
    }
}