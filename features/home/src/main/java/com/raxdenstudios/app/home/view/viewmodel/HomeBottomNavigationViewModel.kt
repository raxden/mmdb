package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.raxdenstudios.app.home.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeBottomNavigationViewModel @Inject constructor() : ViewModel() {

  private val mState = MutableLiveData<UIState>()
  val state: LiveData<UIState> = mState

  init {
    mState.value = UIState(
      bottomUIState = BottomUIState(
        item = HomeBottomMenuItem.HOME,
        onItemSelected = ::onItemSelected,
        onItemReselected = ::onItemReselected,
      )
    )
  }

  private fun onItemSelected(item: Int) {
    when (item) {
      R.id.home -> HomeBottomMenuItem.HOME
      R.id.search -> HomeBottomMenuItem.SEARCH
      R.id.account -> HomeBottomMenuItem.ACCOUNT
    }
  }

  private fun onItemReselected(item: Int) {
    when (item) {
      R.id.home -> HomeBottomMenuItem.HOME
      R.id.search -> HomeBottomMenuItem.SEARCH
      R.id.account -> HomeBottomMenuItem.ACCOUNT
    }
  }

  data class UIState(
    val bottomUIState: BottomUIState
  )

  data class BottomUIState(
    val item: HomeBottomMenuItem,
    val onItemSelected: (itemId: Int) -> Unit,
    val onItemReselected: (itemId: Int) -> Unit,
  )

  enum class HomeBottomMenuItem { HOME, SEARCH, ACCOUNT }
}
