package com.raxdenstudios.app.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.LastRecentSearchesUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.domain.SearchMediasUseCase
import com.raxdenstudios.app.core.ui.mapper.PageListMediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.search.model.SearchBarModel
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.fold
import com.raxdenstudios.commons.core.ext.getValueOrDefault
import com.raxdenstudios.commons.coroutines.ext.safeLaunch
import com.raxdenstudios.commons.pagination.model.PageList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchMediasUseCase: SearchMediasUseCase,
    private val lastRecentSearchesUseCase: LastRecentSearchesUseCase,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase,
    private val pageListMediaModelMapper: PageListMediaModelMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchContract.UIState())
    val uiState: StateFlow<SearchContract.UIState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<SearchContract.UIEvent>()
    val uiEvent: SharedFlow<SearchContract.UIEvent> = _uiEvent.asSharedFlow()

    init {
        loadLastRecentSearches()
    }

    private fun loadLastRecentSearches() {
        viewModelScope.safeLaunch {
            lastRecentSearchesUseCase.invoke()
                .map { resultData -> resultData.getValueOrDefault(emptyList()) }
                .collect { recentSearches -> _uiState.update { value -> value.copy(recentSearches = recentSearches) } }
        }
    }

    fun setUserEvent(event: SearchContract.UserEvent): Unit = when (event) {
        SearchContract.UserEvent.ClearSearchBarClicked -> clearSearchBarClicked()
        SearchContract.UserEvent.ErrorDismissed -> errorDismissed()
        is SearchContract.UserEvent.MediaClicked -> mediaClick(event.media)
        is SearchContract.UserEvent.SearchBarQueryChanged -> performSearch(event.query)
        is SearchContract.UserEvent.SearchClicked -> performSearch(event.query)
        is SearchContract.UserEvent.MediaWatchButtonClicked -> mediaWatchButtonClicked(event.media)
        is SearchContract.UserEvent.RecentSearchClicked -> performSearch(event.query)
    }

    private fun mediaWatchButtonClicked(media: MediaModel) {
        when (media.watchlist) {
            true -> removeMovieFromWatchlist(media)
            false -> addMovieToWatchlist(media)
        }
    }

    private fun addMovieToWatchlist(media: MediaModel) {
        viewModelScope.safeLaunch {
            val params = AddMediaToWatchlistUseCase.Params(media.id, media.mediaType)
            addMediaToWatchlistUseCase(params)
        }
    }

    private fun removeMovieFromWatchlist(media: MediaModel) {
        viewModelScope.safeLaunch {
            val params = RemoveMediaFromWatchlistUseCase.Params(media.id, media.mediaType)
            removeMediaFromWatchlistUseCase(params)
        }
    }

    private fun performSearch(query: String) {
        searching(query)
        viewModelScope.safeLaunch {
            val params = SearchMediasUseCase.Params(query)
            searchMediasUseCase(params)
                .map { result -> pageListMediaModelMapper.transform(result) }
                .collect { result -> result.handleResult(query) }
        }
    }

    private fun ResultData<PageList<MediaModel>, ErrorModel>.handleResult(query: String) {
        fold(
            onSuccess = { pageList -> onSearchSuccess(query, pageList.items) },
            onFailure = { error -> onSearchFailure(query, error) }
        )
    }

    private fun searching(query: String) =
        _uiState.update { value ->
            value.copy(searchBarModel = SearchBarModel.Searching(query))
        }

    private fun onSearchSuccess(query: String, medias: List<MediaModel>) {
        _uiState.update { value ->
            value.copy(
                searchBarModel = when (medias.isEmpty()) {
                    true -> SearchBarModel.WithoutResults(query)
                    false -> SearchBarModel.WithResults(query)
                },
                results = medias,
            )
        }
    }

    private fun onSearchFailure(query: String, error: ErrorModel) {
        _uiState.update { value ->
            value.copy(
                searchBarModel = SearchBarModel.WithoutResults(query),
                error = error
            )
        }
    }

    private fun clearSearchBarClicked() {
        _uiState.update { value ->
            value.copy(
                searchBarModel = SearchBarModel.Focused,
                results = emptyList()
            )
        }
    }

    private fun mediaClick(media: MediaModel) {
        viewModelScope.safeLaunch {
            val event = SearchContract.UIEvent.NavigateToMedia(media.id, media.mediaType)
            _uiEvent.emit(event)
        }
    }

    private fun errorDismissed() {
        _uiState.update { value -> value.copy(error = null) }
    }
}
