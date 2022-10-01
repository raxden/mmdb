package com.raxdenstudios.app.feature.tmdb

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.ConnectUseCase
import com.raxdenstudios.app.core.domain.RequestTokenUseCase
import com.raxdenstudios.core.model.Credentials
import com.raxdenstudios.commons.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TMDBViewModel @Inject constructor(
    private val requestTokenUseCase: RequestTokenUseCase,
    private val connectUseCase: ConnectUseCase,
) : ViewModel() {

    private val mUIState = MutableLiveData<UIState>()
    val uiSTate: LiveData<UIState> = mUIState

    init {
        requestToken()
    }

    private fun requestToken() {
        viewModelScope.launch {
            mUIState.value = UIState.Loading
            when (val resultData = requestTokenUseCase()) {
                is ResultData.Error -> mUIState.value = UIState.Error(resultData.throwable)
                is ResultData.Success -> mUIState.value = UIState.TokenLoaded(resultData.value)
            }
        }
    }

    fun login(token: String) {
        viewModelScope.launch {
            mUIState.value = UIState.Loading
            when (val resultData = connectUseCase(token)) {
                is ResultData.Error -> mUIState.value = UIState.Error(resultData.throwable)
                is ResultData.Success -> mUIState.value = UIState.Connected(resultData.value)
            }
        }
    }

    sealed class UIState {
        object Loading : UIState()
        data class TokenLoaded(val token: String) : UIState()
        data class Connected(val credentials: Credentials) : UIState()
        data class Error(val throwable: Throwable) : UIState()
    }
}
