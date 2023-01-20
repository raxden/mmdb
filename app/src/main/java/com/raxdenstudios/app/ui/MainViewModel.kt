package com.raxdenstudios.app.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(MainContract.UIState())
    val uiState: StateFlow<MainContract.UIState> = _uiState.asStateFlow()

    fun setCurrentRoute(route: String) {
        val shouldShowBottomBar = when (route) {
            "home" -> true
            "mainSearch" -> true
            "mainAccount" -> true
            else -> false
        }
        _uiState.update { value -> value.copy(shouldShowBottomBar = shouldShowBottomBar) }
    }
}
