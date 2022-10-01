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

    fun setUserEvent(event: MainContract.UserEvent) {
        when (event) {
            is MainContract.UserEvent.BottomBarItemSelected -> itemSelected(event)
        }
    }

    fun eventConsumed(event: MainContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }

    private fun itemSelected(event: MainContract.UserEvent.BottomBarItemSelected) {
        _uiState.update { value ->
            value.copy(
                items = value.items.map { item ->
                    item.copyValues(isSelected = item.id == event.item.id)
                },
                events = value.events.plus(MainContract.UIEvent.NavigateTo(event.item.command))
            )
        }
    }
}
