package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.model.MediaListUIState
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.view.mapper.MediaFilterModelToDomainMapper
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.onFailure
import com.raxdenstudios.commons.onSuccess
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

internal class MediaListViewModel(
  private val getMediasUseCase: GetMediasUseCase,
  private val isAccountLoggedUseCase: IsAccountLoggedUseCase,
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase,
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase,
  private val mediaFilterModelToDomainMapper: MediaFilterModelToDomainMapper,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
) : BaseViewModel(), KoinComponent {

  private val pagination: Pagination<MediaListItemModel> by inject { parametersOf(viewModelScope) }

  private val mState = MutableLiveData<MediaListUIState>()
  val state: LiveData<MediaListUIState> = mState

  override fun onCleared() {
    pagination.clear()
    super.onCleared()
  }

  fun addMovieToWatchList(model: MediaListModel, item: MediaListItemModel) =
    viewModelScope.safeLaunch {
      val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Selected)
      val params = AddMediaToWatchListUseCase.Params(item.id, item.mediaType)
      addMediaToWatchListUseCase.execute(params)
        .onFailure { error -> mState.value = MediaListUIState.Error(error) }
        .onSuccess { mState.value = MediaListUIState.Content(model.replaceMovie(itemToReplace)) }
    }

  fun removeMovieFromWatchList(model: MediaListModel, item: MediaListItemModel) =
    viewModelScope.safeLaunch {
      val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Unselected)
      val params = RemoveMediaFromWatchListUseCase.Params(item.id, item.mediaType)
      removeMediaFromWatchListUseCase.execute(params)
        .onFailure { error -> mState.value = MediaListUIState.Error(error) }
        .onSuccess { mState.value = MediaListUIState.Content(model.replaceMovie(itemToReplace)) }
    }

  fun refreshMovies(params: MediaListParams) {
    pagination.clear()
    requestFirstPage(params)
  }

  fun loadMedias(params: MediaListParams) {
    if (dataIsReadyOrLoading()) return
    requestFirstPage(params)
  }

  private fun dataIsReadyOrLoading() =
    mState.value is MediaListUIState.Content || mState.value is MediaListUIState.Loading

  private fun requestFirstPage(params: MediaListParams) {
    requestPage(PageIndex.first, MediaListModel.withFilter(params.mediaFilterModel))
  }

  private fun requestPage(pageIndex: PageIndex, model: MediaListModel) {
    pagination.requestPage(
      pageIndex = pageIndex,
      pageRequest = { page, pageSize -> pageRequest(model.mediaFilterModel, page, pageSize) },
      pageResponse = { pageResult -> pageResponse(model, pageResult) }
    )
  }

  fun loadMoreMovies(pageIndex: PageIndex, model: MediaListModel) {
    requestPage(pageIndex, model)
  }

  private fun pageResponse(model: MediaListModel, pageResult: PageResult<MediaListItemModel>) =
    when (pageResult) {
      is PageResult.Content -> handlePageResultContent(model, pageResult)
      is PageResult.Error -> mState.value = MediaListUIState.Error(pageResult.throwable)
      PageResult.Loading -> mState.value = MediaListUIState.Loading
      PageResult.NoMoreResults -> Unit
      PageResult.NoResults -> mState.value = MediaListUIState.EmptyContent
    }

  private fun handlePageResultContent(
    model: MediaListModel,
    pageResult: PageResult.Content<MediaListItemModel>
  ) {
    viewModelScope.safeLaunch {
      val isAccountLogged = isAccountLoggedUseCase.execute()
      mState.value = MediaListUIState.Content(
        model.copy(
          logged = isAccountLogged,
          media = pageResult.items
        )
      )
    }
  }

  private suspend fun pageRequest(
    mediaFilterModel: MediaFilterModel,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<MediaListItemModel>> {
    val mediaFilter = mediaFilterModelToDomainMapper.transform(mediaFilterModel)
    val useCaseParams = GetMediasUseCase.Params(mediaFilter, page, pageSize)
    return getMediasUseCase.execute(useCaseParams)
      .coMap { pageList -> pageList.map { items -> mediaListItemModelMapper.transform(items) } }
  }
}

