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

@HiltViewModel
internal class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : BaseViewModel() {

    private val mState = MutableLiveData<UIState>()
    val state: LiveData<UIState> = mState

    fun sigIn(credentials: Credentials) {
        viewModelScope.launch(
            onError = { error -> mState.value = UIState.Error(error) }
        ) {
            loginUseCase(credentials)
            mState.value = UIState.Logged
        }
    }

    sealed class UIState {
        object Logged : UIState()
        data class Error(val throwable: Throwable) : UIState()
    }
}
