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
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class HomeMediaListViewModel @Inject constructor(
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMediasUseCase: GetMediasUseCase,
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase,
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase,
  private val getMediasUseCaseParamsMapper: GetMediasUseCaseParamsMapper,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
  private val homeModelMapper: HomeModelMapper,
) : BaseViewModel() {

  private val mState = MutableLiveData<UIState>()
  val state: LiveData<UIState>
    get() {
      if (mState.value == null) loadData()
      return mState
    }

  fun loadData() = viewModelScope.safeLaunch {
    mState.value = UIState.Loading

    getHomeModulesUseCase().collect { modules ->
      val homeModel = homeModelMapper.transform(modules)
      mState.value = UIState.Content(homeModel)
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
    val medias = getMediasUseCase(useCaseParams)
      .map { pageList -> pageList.items }
      .map { medias -> mediaListItemModelMapper.transform(medias) }
      .getValueOrDefault(emptyList())
    val carouselMediasUpdated = carouselMedias.copyWith(mediaType, medias)
    val homeUpdated = model.copy(
      modules = model.modules.replaceItem(carouselMediasUpdated) { it == carouselMedias }
    )
    mState.value = UIState.Content(homeUpdated)
  }

  fun addMediaToWatchList(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Selected)
    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
    addMediaToWatchListUseCase(
      AddMediaToWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
  }

  fun removeMediaFromWatchList(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Unselected)
    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
    removeMediaFromWatchListUseCase(
      RemoveMediaFromWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
  }

  private fun mediaListItemHasChangedThusUpdateHomeMediaListModel(
    home: HomeMediaListModel,
    mediaListItem: MediaListItemModel,
  ) {
    val homeUpdated = home.updateMedia(mediaListItem)
    mState.value = UIState.Content(homeUpdated)
  }

  internal sealed class UIState {
    object Loading : UIState()
    data class Content(val model: HomeMediaListModel) : UIState()
    data class Error(val throwable: Throwable) : UIState()
  }
}
