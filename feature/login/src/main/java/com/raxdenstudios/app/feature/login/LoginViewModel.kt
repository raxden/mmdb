package com.raxdenstudios.app.feature.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.core.model.Credentials
import com.raxdenstudios.app.core.domain.LoginUseCase
import com.raxdenstudios.commons.ext.launch
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
) : ViewModel() {

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
