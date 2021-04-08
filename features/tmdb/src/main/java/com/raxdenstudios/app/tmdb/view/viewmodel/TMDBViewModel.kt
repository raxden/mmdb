package com.raxdenstudios.app.tmdb.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.tmdb.domain.ConnectUseCase
import com.raxdenstudios.app.tmdb.domain.RequestTokenUseCase
import com.raxdenstudios.commons.ResultData
import kotlinx.coroutines.launch

sealed class TMDBConnectUIState {
  object Loading : TMDBConnectUIState()
  data class TokenLoaded(val token: String) : TMDBConnectUIState()
  data class Connected(val credentials: Credentials) : TMDBConnectUIState()
  data class Error(val throwable: Throwable) : TMDBConnectUIState()
}

internal class TMDBViewModel(
  private val requestTokenUseCase: RequestTokenUseCase,
  private val connectUseCase: ConnectUseCase,
) : BaseViewModel() {

  private val mState = MutableLiveData<TMDBConnectUIState>()
  val state: LiveData<TMDBConnectUIState> = mState

  init {
    requestToken()
  }

  private fun requestToken() {
    viewModelScope.launch {
      mState.value = TMDBConnectUIState.Loading
      when (val resultData = requestTokenUseCase.execute()) {
        is ResultData.Error -> mState.value = TMDBConnectUIState.Error(resultData.throwable)
        is ResultData.Success -> mState.value = TMDBConnectUIState.TokenLoaded(resultData.value)
      }
    }
  }

  fun login(token: String) {
    viewModelScope.launch {
      mState.value = TMDBConnectUIState.Loading
      when (val resultData = connectUseCase.execute(token)) {
        is ResultData.Error -> mState.value = TMDBConnectUIState.Error(resultData.throwable)
        is ResultData.Success -> mState.value = TMDBConnectUIState.Connected(resultData.value)
      }
    }
  }
}
