package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModelMapper
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.onFailure
import com.raxdenstudios.commons.ext.replaceItem
import com.raxdenstudios.commons.ext.safeLaunch

internal sealed class HomeMediaListUIState {
  object Loading : HomeMediaListUIState()
  data class Content(val model: HomeMediaListModel) : HomeMediaListUIState()
  data class Error(val throwable: Throwable) : HomeMediaListUIState()
}

internal class HomeMediaListViewModel(
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMediasUseCase: GetMediasUseCase,
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase,
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase,
  private val getMediasUseCaseParamsMapper: GetMediasUseCaseParamsMapper,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
  private val homeModelMapper: HomeModelMapper,
) : BaseViewModel() {

  private val mState = MutableLiveData<HomeMediaListUIState>()
  val state: LiveData<HomeMediaListUIState>
    get() {
      if (mState.value == null) loadData()
      return mState
    }

  fun loadData() = viewModelScope.safeLaunch {
    mState.value = HomeMediaListUIState.Loading

    getHomeModulesUseCase.execute().collect { modules ->
      val homeModel = homeModelMapper.transform(modules)
      mState.value = HomeMediaListUIState.Content(homeModel)
    }
  }

  fun mediaSelected(
    model: HomeMediaListModel,
    carouselMedias: HomeModuleModel.CarouselMedias,
    mediaItemModel: MediaListItemModel,
  ) {

  }

  fun filterChanged(
    model: HomeMediaListModel,
    carouselMedias: HomeModuleModel.CarouselMedias,
    mediaType: MediaType
  ) = viewModelScope.safeLaunch {
    val useCaseParams = getMediasUseCaseParamsMapper.transform(carouselMedias, mediaType)
    val medias = getMediasUseCase.execute(useCaseParams)
      .map { pageList -> pageList.items }
      .map { medias -> mediaListItemModelMapper.transform(medias) }
      .getValueOrDefault(emptyList())
    val carouselMediasUpdated = carouselMedias.copyWith(mediaType, medias)
    val homeUpdated = model.copy(
      modules = model.modules.replaceItem(carouselMediasUpdated) { it == carouselMedias }
    )
    mState.value = HomeMediaListUIState.Content(homeUpdated)
  }

  fun addMediaToWatchList(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Selected)
    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
    addMediaToWatchListUseCase.execute(
      AddMediaToWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
  }

  fun removeMediaFromWatchList(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Unselected)
    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
    removeMediaFromWatchListUseCase.execute(
      RemoveMediaFromWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
  }

  private fun mediaListItemHasChangedThusUpdateHomeMediaListModel(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) {
    val homeUpdated = home.updateMedia(mediaListItem)
    mState.value = HomeMediaListUIState.Content(homeUpdated)
  }
}
