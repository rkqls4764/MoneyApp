package com.gabeen.moneyapp.ui.home.setting

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gabeen.moneyapp.data.util.CsvImporter
import com.gabeen.moneyapp.ui.effect.UiEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.xml.sax.ErrorHandler
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(private val csvImporter: CsvImporter) : ViewModel() {
    companion object {
        private const val TAG = "SettingViewModel"
    }

    private val _uiEffect = MutableSharedFlow<UiEffect>(extraBufferCapacity = 1)
    val uiEffect = _uiEffect.asSharedFlow()

    private val _settingState = MutableStateFlow(SettingState())
    val settingState = _settingState.asStateFlow()

    /* csv 파일 가져오기 */
    fun importCsv(context: Context, uri: Uri) {
        viewModelScope.launch {
            _settingState.update { it.copy(isLoading = true) }

            try {
                csvImporter.importCsv(uri, context)

                _uiEffect.tryEmit(UiEffect.ShowToast("업로드가 완료되었습니다"))
                Log.d(TAG, "[importCsv] csv 파일 업로드 성공")

                _settingState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                _uiEffect.tryEmit(UiEffect.ShowToast("업로드에 실패했습니다"))
                Log.d(TAG, "[importCsv] csv 파일 업로드 실패\n${e}")

                _settingState.update { it.copy(isLoading = false) }
            }
        }
    }
}