package com.raxdenstudios.app.tmdb.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.tmdb.domain.ConnectUseCase
import com.raxdenstudios.app.tmdb.domain.RequestTokenUseCase
import com.raxdenstudios.commons.ResultData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TMDBViewModel @Inject constructor(
  private val requestTokenUseCase: RequestTokenUseCase,
  private val connectUseCase: ConnectUseCase,
) : BaseViewModel() {

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

  internal sealed class UIState {
    object Loading : UIState()
    data class TokenLoaded(val token: String) : UIState()
    data class Connected(val credentials: Credentials) : UIState()
    data class Error(val throwable: Throwable) : UIState()
  }
}
