package com.raxdenstudios.app.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.ui.model.BottomBarItemModel
import com.raxdenstudios.commons.coroutines.ext.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BottomBarContract.UIState())
    val uiState: StateFlow<BottomBarContract.UIState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<BottomBarContract.UIEvent>()
    val uiEvent: SharedFlow<BottomBarContract.UIEvent> = _uiEvent.asSharedFlow()

    fun setUserEvent(event: BottomBarContract.UserEvent) {
        when (event) {
            is BottomBarContract.UserEvent.ItemSelected -> itemSelected(event.item)
        }
    }

    private fun itemSelected(itemSelected: BottomBarItemModel) {
        _uiState.update { value ->
            value.copy(
                items = value.items.map { item ->
                    item.copyValues(isSelected = item.id == itemSelected.id)
                },
            )
        }
        viewModelScope.safeLaunch {
            val event = when (itemSelected) {
                is BottomBarItemModel.Account -> BottomBarContract.UIEvent.NavigateToAccount
                is BottomBarItemModel.Home -> BottomBarContract.UIEvent.NavigateToHome
                is BottomBarItemModel.Search -> BottomBarContract.UIEvent.NavigateToSearch
            }
            _uiEvent.emit(event)
        }
    }
}
