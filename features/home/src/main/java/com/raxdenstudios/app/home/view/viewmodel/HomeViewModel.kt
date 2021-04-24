package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.view.mapper.MediaFilterModelToDomainMapper
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.replaceItem
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.getValueOrDefault
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.onFailure
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect

internal class HomeViewModel(
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMediasUseCase: GetMediasUseCase,
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase,
  private val isAccountLoggedUseCase: IsAccountLoggedUseCase,
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase,
  private val getMediasUseCaseParamsMapper: GetMediasUseCaseParamsMapper,
  private val homeModuleModelMapper: HomeModuleModelMapper,
  private val mediaFilterModelToDomainMapper: MediaFilterModelToDomainMapper,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
) : BaseViewModel() {

  private val mState = MutableLiveData<HomeUIState>()
  val state: LiveData<HomeUIState> = mState

  init {
    loadData()
  }

  private fun loadData() = viewModelScope.safeLaunch {
    mState.value = HomeUIState.Loading

    getHomeModulesUseCase.execute().collect { modules ->
      val accountIsLogged = isAccountLoggedUseCase.execute()
      val homeModuleListModel = modules.map { homeModule ->
        async { getDataFromModule(homeModule) }
      }.mapNotNull { deferred ->
        deferred.await()
      }
      val model = HomeModel(accountIsLogged, homeModuleListModel)
      mState.value = HomeUIState.Content(model)
    }
  }

  private suspend fun getDataFromModule(module: HomeModule): HomeModuleModel? {
    val useCaseParams = getMediasUseCaseParamsMapper.transform(module)
    val resultData = getMediasUseCase.execute(useCaseParams)
    return homeModuleModelMapper.transform(module, resultData)
  }

  fun refreshData() {
    loadData()
  }

  fun mediaSelected(
    model: HomeModel,
    homeModuleModel: HomeModuleModel.CarouselMedias,
    carouselMoviesModel: CarouselMediaListModel,
    mediaItemModel: MediaListItemModel,
  ) {

  }

  fun filterChanged(
    home: HomeModel,
    homeModule: HomeModuleModel.CarouselMedias,
    carouselMediaList: CarouselMediaListModel,
    mediaFilterModel: MediaFilterModel
  ) = viewModelScope.safeLaunch {
    val mediaFilter = mediaFilterModelToDomainMapper.transform(mediaFilterModel)
    val medias = getMediasUseCase.execute(GetMediasUseCase.Params(mediaFilter))
      .map { pageList -> pageList.items }
      .map { medias -> mediaListItemModelMapper.transform(medias) }
      .getValueOrDefault(emptyList())
    val carouselMediaListUpdated = carouselMediaList.copy(
      mediaFilterModel = mediaFilterModel,
      medias = medias,
    )
    val homeModuleUpdated = homeModule.copy(carouselMediaListModel = carouselMediaListUpdated)
    val homeUpdated =
      home.copy(modules = home.modules.replaceItem(homeModuleUpdated) { it == homeModule })
    mState.value = HomeUIState.Content(homeUpdated)
  }

  fun addMediaToWatchList(
    home: HomeModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Selected)
    mediaListItemHasChangedThusUpdateHomeModel(home, itemUpdated)
    addMediaToWatchListUseCase.execute(
      AddMediaToWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeModel(home, mediaListItem) }
  }

  fun removeMediaFromWatchList(
    home: HomeModel,
    mediaListItem: MediaListItemModel,
  ) = viewModelScope.safeLaunch {
    val itemUpdated = mediaListItem.copy(watchButtonModel = WatchButtonModel.Unselected)
    mediaListItemHasChangedThusUpdateHomeModel(home, itemUpdated)
    removeMediaFromWatchListUseCase.execute(
      RemoveMediaFromWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
    ).onFailure { mediaListItemHasChangedThusUpdateHomeModel(home, mediaListItem) }
  }

  private fun mediaListItemHasChangedThusUpdateHomeModel(
    home: HomeModel,
    mediaListItem: MediaListItemModel,
  ) {
    val homeUpdated = home.updateMedia(mediaListItem)
    mState.value = HomeUIState.Content(homeUpdated)
  }
}
