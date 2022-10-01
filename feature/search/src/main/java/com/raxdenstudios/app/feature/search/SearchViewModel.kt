package com.raxdenstudios.app.feature.search

import androidx.lifecycle.ViewModel
import com.raxdenstudios.app.feature.search.SearchContract
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SearchContract.UIState())
    val uiState: StateFlow<SearchContract.UIState> = _uiState.asStateFlow()
}
