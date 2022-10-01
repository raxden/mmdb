package com.raxdenstudios.app.feature.account

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AccountContract.UIState())
    val uiState: StateFlow<AccountContract.UIState> = _uiState.asStateFlow()
}
