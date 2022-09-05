package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.list.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.list.view.mapper.MediaListModelMapper
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.onFailure
import com.raxdenstudios.commons.ext.onSuccess
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
internal class MediaListViewModel @Inject constructor(
    private val mediaListParamsFactory: MediaListParamsFactory,
    private val paginationConfig: Pagination.Config,
    private val getMediasUseCase: GetMediasUseCase,
    private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase,
    private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase,
    private val getMediasUseCaseParamsMapper: GetMediasUseCaseParamsMapper,
    private val mediaListModelMapper: MediaListModelMapper,
) : BaseViewModel() {

    private val pagination: CoPagination<Media> = CoPagination(
        config = paginationConfig,
        logger = { message -> Timber.tag("Pagination").d(message) },
        coroutineScope = viewModelScope,
    )
    private val params: MediaListParams by lazy { mediaListParamsFactory.create() }

    private val _uiState = MutableStateFlow(MediaListContract.UIState.loading())
    val uiState: StateFlow<MediaListContract.UIState> = _uiState.asStateFlow()

    init {
        pagination.requestFirstPage(params)
    }

    override fun onCleared() {
        pagination.clear()
        super.onCleared()
    }

    fun setUserEvent(event: MediaListContract.UserEvent) {
        when (event) {
            is MediaListContract.UserEvent.OnLoadMore -> requestPage(event.pageIndex, params)
            is MediaListContract.UserEvent.OnRefresh -> pagination.refresh(params)
            is MediaListContract.UserEvent.OnWatchButtonClicked -> {
                when (event.item.watchButtonModel) {
                    is WatchButtonModel.Selected -> removeMovieFromWatchList(event.item)
                    is WatchButtonModel.Unselected -> addMovieToWatchList(event.item)
                }
            }
        }
    }

    private fun addMovieToWatchList(item: MediaListItemModel) =
        viewModelScope.safeLaunch {
            val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Selected)
            val params = AddMediaToWatchListUseCase.Params(item.id, item.mediaType)
            addMediaToWatchListUseCase(params)
                .onFailure { error -> _uiState.update { value -> value.copy(error = error) } }
                .onSuccess { _uiState.update { value -> value.copy(model = value.model.replaceMovie(itemToReplace)) } }
        }

    private fun removeMovieFromWatchList(item: MediaListItemModel) =
        viewModelScope.safeLaunch {
            val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Unselected)
            val params = RemoveMediaFromWatchListUseCase.Params(item.id, item.mediaType)
            removeMediaFromWatchListUseCase(params)
                .onFailure { error -> _uiState.update { value -> value.copy(error = error) } }
                .onSuccess { _uiState.update { value -> value.copy(model = value.model.replaceMovie(itemToReplace)) } }
        }

    private fun CoPagination<Media>.refresh(params: MediaListParams) {
        pagination.clear()
        pagination.requestFirstPage(params)
    }

    private fun CoPagination<Media>.requestFirstPage(params: MediaListParams) {
        requestFirstPage(
            pageRequest = { page, pageSize -> pageRequest(params, page, pageSize) },
            pageResponse = { pageResult -> pageResponse(params, pageResult) }
        )
    }

    private fun requestPage(pageIndex: PageIndex, params: MediaListParams) {
        pagination.requestPage(
            pageIndex = pageIndex,
            pageRequest = { page, pageSize -> pageRequest(params, page, pageSize) },
            pageResponse = { pageResult -> pageResponse(params, pageResult) }
        )
    }

    private fun pageResponse(params: MediaListParams, pageResult: PageResult<Media>) =
        when (pageResult) {
            is PageResult.Content -> handlePageResultContent(params, pageResult)
            is PageResult.Error -> _uiState.update { value -> value.copy(error = pageResult.throwable) }
            PageResult.Loading -> _uiState.update { value -> value.copy(isLoading = true) }
            PageResult.NoMoreResults -> Unit
            PageResult.NoResults -> _uiState.update { value ->
                value.copy(
                    isLoading = false,
                    model = value.model.copy(items = emptyList())
                )
            }
        }

    private fun handlePageResultContent(
        params: MediaListParams,
        pageResult: PageResult.Content<Media>,
    ) {
        viewModelScope.safeLaunch {
            val model = mediaListModelMapper.transform(params, pageResult.items)
            _uiState.update { value -> value.copy(isLoading = false, model = model) }
        }
    }

    private suspend fun pageRequest(
        params: MediaListParams,
        page: Page,
        pageSize: PageSize,
    ): PageList<Media> {
        val useCaseParams = getMediasUseCaseParamsMapper.transform(params, page, pageSize)
        return getMediasUseCase(useCaseParams)
            .getValueOrDefault(PageList(emptyList(), page))
    }
}
