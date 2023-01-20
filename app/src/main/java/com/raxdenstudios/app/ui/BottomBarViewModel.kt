package com.raxdenstudios.app.ui

import androidx.lifecycle.ViewModel
import com.raxdenstudios.app.core.navigation.MainDirections
import com.raxdenstudios.app.ui.model.BottomBarItemModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class BottomBarViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(BottomBarContract.UIState())
    val uiState: StateFlow<BottomBarContract.UIState> = _uiState.asStateFlow()

    fun setUserEvent(event: BottomBarContract.UserEvent) {
        when (event) {
            is BottomBarContract.UserEvent.ItemSelected -> itemSelected(event.item)
        }
    }

    fun eventConsumed(event: BottomBarContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }

    private fun itemSelected(itemSelected: BottomBarItemModel) {
        val event = when (itemSelected) {
            is BottomBarItemModel.Account -> BottomBarContract.UIEvent.NavigateToAccount
            is BottomBarItemModel.Home -> BottomBarContract.UIEvent.NavigateToHome
            is BottomBarItemModel.Search -> BottomBarContract.UIEvent.NavigateToSearch
        }
        _uiState.update { value ->
            value.copy(
                items = value.items.map { item ->
                    item.copyValues(isSelected = item.id == itemSelected.id)
                },
                events = value.events.plus(event)
            )
        }
    }
}
