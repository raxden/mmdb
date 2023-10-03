package com.raxdenstudios.app.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediasUseCase
import com.raxdenstudios.app.core.domain.GetRelatedMediasUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.mapper.MediaListTitleModelMapper
import com.raxdenstudios.app.feature.model.MediaListParams
import com.raxdenstudios.commons.core.ext.getValueOrDefault
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import com.raxdenstudios.commons.core.ext.onFailure
import com.raxdenstudios.commons.core.ext.onSuccess
import com.raxdenstudios.commons.core.ext.replaceItem
import com.raxdenstudios.commons.coroutines.ext.safeLaunch
import com.raxdenstudios.commons.pagination.CoPagination
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageResult
import com.raxdenstudios.commons.pagination.model.PageSize
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
@Suppress("LongParameterList")
class MediaListViewModel @Inject constructor(
    private val mediaListParamsFactory: MediaListParamsFactory,
    private val paginationConfig: Pagination.Config,
    private val getMediasUseCase: GetMediasUseCase,
    private val getRelatedMediasUseCase: GetRelatedMediasUseCase,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase,
    private val mediaListTitleModelMapper: MediaListTitleModelMapper,
    private val mediaModelMapper: MediaModelMapper,
    private val errorModelMapper: ErrorModelMapper,
) : ViewModel() {

    private val pagination: CoPagination<Media> = CoPagination(
        config = paginationConfig,
        logger = { message -> Timber.tag("Pagination").d(message) },
        coroutineScope = viewModelScope,
    )
    private val params: MediaListParams by lazy { mediaListParamsFactory.create() }

    private val _uiState = MutableStateFlow(MediaListContract.UIState.loading())
    val uiState: StateFlow<MediaListContract.UIState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MediaListContract.UIEvent>()
    val uiEvent: SharedFlow<MediaListContract.UIEvent> = _uiEvent.asSharedFlow()

    init {
        setTitle(params)
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

        MediaListContract.UserEvent.BackClicked -> navigateToBack()
        is MediaListContract.UserEvent.MediaClicked -> navigateToMedia(event.item)
        MediaListContract.UserEvent.ErrorDismissed -> errorDismissed()
    }

    private fun navigateToBack() {
        viewModelScope.safeLaunch {
            _uiEvent.emit(MediaListContract.UIEvent.NavigateToBack)
        }
    }

    private fun navigateToMedia(media: MediaModel) {
        viewModelScope.safeLaunch {
            _uiEvent.emit(MediaListContract.UIEvent.NavigateToMedia(media.id, media.mediaType))
        }
    }

    private fun addMovieToWatchlist(item: MediaModel) {
        viewModelScope.safeLaunch {
            val params = AddMediaToWatchlistUseCase.Params(item.id, item.mediaType)
            addMediaToWatchlistUseCase(params)
                .map { media -> mediaModelMapper.transform(media) }
                .mapFailure { error -> errorModelMapper.transform(error) }
                .onSuccess { item -> updateMediaItem(item) }
                .onFailure { error -> handleError(error) }
        }
    }

    private fun updateMediaItem(item: MediaModel) {
        _uiState.update { value -> value.replaceItem(item) }
    }

    private fun MediaListContract.UIState.replaceItem(itemToReplace: MediaModel) = copy(
        items = this.items.replaceItem(itemToReplace) { it.id == itemToReplace.id }
    )

    private fun removeMovieFromWatchlist(item: MediaModel) {
        viewModelScope.safeLaunch {
            val itemToReplace = item.copy(watchlist = false)
            val params = RemoveMediaFromWatchlistUseCase.Params(item.id, item.mediaType)
            removeMediaFromWatchlistUseCase(params)
                .map { itemToReplace }
                .mapFailure { error -> errorModelMapper.transform(error) }
                .onSuccess { item -> updateMediaItem(item) }
                .onFailure { error -> handleError(error) }
        }
    }

    private fun refresh(params: MediaListParams) {
        pagination.clear()
        requestFirstPage(params)
    }

    private fun setTitle(params: MediaListParams) {
        viewModelScope.safeLaunch {
            _uiState.update { value -> value.copy(title = mediaListTitleModelMapper.transform(params)) }
        }
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
            is PageResult.Error -> handleError(errorModelMapper.transform(pageResult.throwable))
            PageResult.Loading -> Unit
            PageResult.NoMoreResults -> _uiState.update { value -> value.copy(isLoading = false) }
            PageResult.NoResults -> _uiState.update { value -> value.copy(isLoading = false, items = emptyList()) }
        }

    private fun handleError(error: ErrorModel) {
        _uiState.update { value -> value.copy(error = error) }
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
    ): PageList<Media> =
        when (params) {
            is MediaListParams.List -> {
                val useCaseParams = GetMediasUseCase.Params(params.mediaFilter, page, pageSize)
                getMediasUseCase(useCaseParams)
                    .getValueOrDefault(PageList(emptyList(), page))
            }

            is MediaListParams.Related -> {
                val useCaseParams = GetRelatedMediasUseCase.Params(params.mediaId, params.mediaType, page, pageSize)
                getRelatedMediasUseCase(useCaseParams).first()
                    .getValueOrDefault(PageList(emptyList(), page))
            }
        }

    private fun errorDismissed() {
        _uiState.update { value -> value.copy(error = null) }
    }
}
