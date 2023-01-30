package com.raxdenstudios.app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.feature.mapper.MediaListTitleModelMapper
import com.raxdenstudios.app.feature.model.MediaListParams
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediasUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.onFailure
import com.raxdenstudios.commons.ext.onSuccess
import com.raxdenstudios.commons.ext.replaceItem
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.pagination.CoPagination
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@Suppress("LongParameterList")
class MediaListViewModel @Inject constructor(
    private val mediaListParamsFactory: MediaListParamsFactory,
    private val paginationConfig: Pagination.Config,
    private val getMediasUseCase: GetMediasUseCase,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase,
    private val mediaListTitleModelMapper: MediaListTitleModelMapper,
    private val mediaModelMapper: MediaModelMapper,
) : ViewModel() {

    private val pagination: CoPagination<Media> = CoPagination(
        config = paginationConfig,
        logger = { message -> Timber.tag("Pagination").d(message) },
        coroutineScope = viewModelScope,
    )
    private val params: MediaListParams by lazy { mediaListParamsFactory.create() }

    private val _uiState = MutableStateFlow(MediaListContract.UIState.loading())
    val uiState: StateFlow<MediaListContract.UIState> = _uiState.asStateFlow()

    init {
        _uiState.update { value -> value.copy(title = mediaListTitleModelMapper.transform(params)) }
        requestFirstPage(params)
    }

    override fun onCleared() {
        pagination.clear()
        super.onCleared()
    }

    fun setUserEvent(event: MediaListContract.UserEvent): Unit = when (event) {
        is MediaListContract.UserEvent.LoadMore -> requestPage(event.pageIndex, params)
        is MediaListContract.UserEvent.Refresh -> refresh(params)
        is MediaListContract.UserEvent.WatchButtonClicked -> when (event.item.watchlist) {
            true -> removeMovieFromWatchlist(event.item)
            false -> addMovieToWatchlist(event.item)
        }
        MediaListContract.UserEvent.BackClicked ->
            updateUIStateWithEvent(MediaListContract.UIEvent.NavigateToBack)
        is MediaListContract.UserEvent.MediaClicked ->
            updateUIStateWithEvent(MediaListContract.UIEvent.NavigateToMedia(event.item.id, event.item.mediaType))
    }

    private fun updateUIStateWithEvent(event: MediaListContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.plus(event)) }
    }

    private fun addMovieToWatchlist(item: MediaModel) {
        viewModelScope.safeLaunch {
            val itemToReplace = item.copy(watchlist = true)
            val params = AddMediaToWatchlistUseCase.Params(item.id, item.mediaType)
            addMediaToWatchlistUseCase(params)
                .onFailure { error -> _uiState.update { value -> value.copy(error = error) } }
                .onSuccess { _uiState.update { value -> value.replaceItem(itemToReplace) } }
        }
    }

    private fun MediaListContract.UIState.replaceItem(itemToReplace: MediaModel) = copy(
        items = this.items.replaceItem(itemToReplace) { it.id == itemToReplace.id }
    )

    private fun removeMovieFromWatchlist(item: MediaModel) {
        viewModelScope.safeLaunch {
            val itemToReplace = item.copy(watchlist = false)
            val params = RemoveMediaFromWatchlistUseCase.Params(item.id, item.mediaType)
            removeMediaFromWatchlistUseCase(params)
                .onFailure { error -> _uiState.update { value -> value.copy(error = error) } }
                .onSuccess { _uiState.update { value -> value.replaceItem(itemToReplace) } }
        }
    }

    private fun refresh(params: MediaListParams) {
        pagination.clear()
        requestFirstPage(params)
    }

    private fun requestFirstPage(params: MediaListParams) {
        _uiState.update { value -> value.copy(isLoading = true) }
        pagination.requestFirstPage(
            pageRequest = { page, pageSize -> pageRequest(params, page, pageSize) },
            pageResponse = { pageResult -> pageResponse(pageResult) }
        )
    }

    private fun requestPage(pageIndex: PageIndex, params: MediaListParams) {
        pagination.requestPage(
            pageIndex = pageIndex,
            pageRequest = { page, pageSize -> pageRequest(params, page, pageSize) },
            pageResponse = { pageResult -> pageResponse(pageResult) }
        )
    }

    private fun pageResponse(pageResult: PageResult<Media>) =
        when (pageResult) {
            is PageResult.Content -> handlePageResultContent(pageResult)
            is PageResult.Error -> handlePageResultError(pageResult)
            PageResult.Loading -> Unit
            PageResult.NoMoreResults -> handlePageNoMoreResults()
            PageResult.NoResults -> handlePageResultNoResults()
        }

    private fun handlePageResultError(pageResult: PageResult.Error) {
        _uiState.update { value -> value.copy(error = pageResult.throwable) }
    }

    private fun handlePageNoMoreResults() {
        _uiState.update { value -> value.copy(isLoading = false) }
    }

    private fun handlePageResultNoResults() {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                items = emptyList()
            )
        }
    }

    private fun handlePageResultContent(
        pageResult: PageResult.Content<Media>,
    ) {
        viewModelScope.safeLaunch {
            _uiState.update { value ->
                value.copy(
                    isLoading = false,
                    items = mediaModelMapper.transform(pageResult.items)
                )
            }
        }
    }

    private suspend fun pageRequest(
        params: MediaListParams,
        page: Page,
        pageSize: PageSize,
    ): PageList<Media> {
        val useCaseParams = GetMediasUseCase.Params(params.mediaFilter, page, pageSize)
        return getMediasUseCase(useCaseParams)
            .getValueOrDefault(PageList(emptyList(), page))
    }

    fun eventConsumed(event: MediaListContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }
}
