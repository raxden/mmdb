package com.raxdenstudios.app.login.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.login.domain.LoginUseCase
import com.raxdenstudios.commons.ext.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

sealed class LoginUIState {
  object Logged : LoginUIState()
  data class Error(val throwable: Throwable) : LoginUIState()
}

@HiltViewModel
internal class LoginViewModel @Inject constructor(
  private val loginUseCase: LoginUseCase
) : BaseViewModel() {

  private val mState = MutableLiveData<LoginUIState>()
  val state: LiveData<LoginUIState> = mState

  fun sigIn(credentials: Credentials) {
    viewModelScope.launch(
      onError = { error -> mState.value = LoginUIState.Error(error) }
    ) {
      loginUseCase.execute(credentials)
      mState.value = LoginUIState.Logged
    }
  }
}
