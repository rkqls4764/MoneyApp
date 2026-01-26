package com.gabeen.moneyapp.ui.category

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabeen.moneyapp.data.repository.CategoryRepository
import com.gabeen.moneyapp.ui.category.add.CategoryAddEvent
import com.gabeen.moneyapp.ui.category.add.CategoryAddReducer
import com.gabeen.moneyapp.ui.category.add.CategoryAddState
import com.gabeen.moneyapp.ui.category.detail.CategoryDetailEvent
import com.gabeen.moneyapp.ui.category.detail.CategoryDetailReducer
import com.gabeen.moneyapp.ui.category.detail.CategoryDetailState
import com.gabeen.moneyapp.ui.category.edit.CategoryEditEvent
import com.gabeen.moneyapp.ui.category.edit.CategoryEditReducer
import com.gabeen.moneyapp.ui.category.edit.CategoryEditState
import com.gabeen.moneyapp.ui.category.manage.CategoryManageEvent
import com.gabeen.moneyapp.ui.category.manage.CategoryManageReducer
import com.gabeen.moneyapp.ui.category.manage.CategoryManageState
import com.gabeen.moneyapp.ui.effect.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private val categoryRepository: CategoryRepository) : ViewModel() {
    companion object {
        private const val TAG = "CategoryViewModel"
    }

    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val _categoryAddState = MutableStateFlow(CategoryAddState())
    val categoryAddState = _categoryAddState.asStateFlow()
    private val _categoryDetailState = MutableStateFlow(CategoryDetailState())
    val categoryDetailState = _categoryDetailState.asStateFlow()
    private val _categoryEditState = MutableStateFlow(CategoryEditState())
    val categoryEditState = _categoryEditState.asStateFlow()
    private val _categoryManageState = MutableStateFlow(CategoryManageState())
    val categoryManageState = _categoryManageState.asStateFlow()

    fun onAddEvent(e: CategoryAddEvent) {
        _categoryAddState.update { CategoryAddReducer.reduce(it, e) }

        when (e) {
            CategoryAddEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            CategoryAddEvent.ClickedAdd -> addCategory()
            else -> Unit
        }
    }

    fun onDetailEvent(e: CategoryDetailEvent) {
        _categoryDetailState.update { CategoryDetailReducer.reduce(it, e) }

        when (e) {
            CategoryDetailEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            CategoryDetailEvent.ClickedEdit -> _uiEffect.tryEmit(UiEffect.Navigate("categoryEdit"))
            CategoryDetailEvent.ClickedDelete -> deleteCategory()
            else -> Unit
        }
    }

    fun onEditEvent(e: CategoryEditEvent) {
        _categoryEditState.update { CategoryEditReducer.reduce(it, e) }

        when (e) {
            CategoryEditEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            CategoryEditEvent.ClickedUpdate -> updateCategory()
            else -> Unit
        }
    }

    fun onManageEvent(e: CategoryManageEvent) {
        _categoryManageState.update { CategoryManageReducer.reduce(it, e) }

        when (e) {
            CategoryManageEvent.Init -> getAllCategories()
            CategoryManageEvent.ClickedBack -> _uiEffect.tryEmit(UiEffect.NavigateBack)
            CategoryManageEvent.ClickedAdd -> _uiEffect.tryEmit(UiEffect.Navigate("categoryAdd"))
            CategoryManageEvent.ClickedCategory -> _uiEffect.tryEmit(UiEffect.Navigate("categoryDetail"))
            else -> Unit
        }
    }

    /* 카테고리 추가 */
    fun addCategory() {
        viewModelScope.launch {
            categoryRepository.insert(
                category = categoryAddState.value.inputData
            )

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("카테고리가 추가되었습니다"))

            Log.d(TAG, "[addCategory] 카테고리 추가 성공\n${categoryAddState.value.inputData}")
        }
    }

    /* 카테고리 수정 */
    fun updateCategory() {
        viewModelScope.launch {
            categoryRepository.update(
                category = categoryEditState.value.inputData
            )

            _categoryDetailState.update { it.copy(categoryInfo = categoryEditState.value.inputData) }

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("카테고리가 수정되었습니다"))

            Log.d(TAG, "[updateCategory] 카테고리 수정 성공\n${categoryEditState.value.inputData}")
        }
    }

    /* 카테고리 삭제 */
    fun deleteCategory() {
        viewModelScope.launch {
            categoryRepository.delete(
                category = categoryDetailState.value.categoryInfo
            )

            _uiEffect.emit(UiEffect.NavigateBack)
            _uiEffect.emit(UiEffect.ShowToast("카테고리가 삭제되었습니다"))

            Log.d(TAG, "[deleteCategory] 카테고리 삭제 성공\n${categoryDetailState.value.categoryInfo}")
        }
    }

    /* 카테고리 전체 조회 */
    fun getAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collect { data ->
                _categoryManageState.update { it.copy(categories = data) }

                Log.d(TAG, "[addCategory] 카테고리 전체 조회 성공\n${data}")
            }
        }
    }
}