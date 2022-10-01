package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModelMapper
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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
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

    private val _uiState = MutableStateFlow(HomeMediaListContract.UIState.loading())
    val uiState: StateFlow<HomeMediaListContract.UIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.safeLaunch {
        getHomeModulesUseCase()
            .catch { error -> _uiState.update { value -> value.copy(error = error) } }
            .map { modules -> homeModelMapper.transform(modules) }
            .collect { model -> _uiState.update { value -> value.copy(isLoading = false, model = model) } }
    }

    fun setUserEvent(event: HomeMediaListContract.UserEvent): Unit = when (event) {
        is HomeMediaListContract.UserEvent.MediaSelected -> Unit
        is HomeMediaListContract.UserEvent.WatchButtonClicked -> when (event.item.watchButton) {
            is WatchButtonModel.Selected -> removeMovieFromWatchList(event.item)
            is WatchButtonModel.Unselected -> addMovieToWatchList(event.item)
        }
        is HomeMediaListContract.UserEvent.ViewAllButtonClicked -> viewAllButtonSelected(event.module)
        is HomeMediaListContract.UserEvent.FilterChanged -> filterChanged(event.module, event.mediaType)
    }

    private fun viewAllButtonSelected(module: HomeModuleModel.CarouselMedias) {
        _uiState.update { value ->
            value.copy(events = value.events.plus(HomeMediaListContract.UIEvent.NavigateToMediaList(module)))
        }
    }

    fun eventConsumed(event: HomeMediaListContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }

    private fun filterChanged(
        carouselMedias: HomeModuleModel.CarouselMedias,
        mediaType: MediaType,
    ) {
        viewModelScope.safeLaunch {
            val useCaseParams = getMediasUseCaseParamsMapper.transform(carouselMedias, mediaType)
            val medias = getMediasUseCase(useCaseParams)
                .map { pageList -> pageList.items }
                .map { medias -> mediaListItemModelMapper.transform(medias) }
                .getValueOrDefault(emptyList())
            val carouselMediasUpdated = carouselMedias.copyWith(mediaType, medias)
            _uiState.update { value ->
                value.copy(
                    model = value.model.copy(
                        modules = value.model.modules.replaceItem(carouselMediasUpdated) { it == carouselMedias }
                    )
                )
            }
        }
    }

    private fun addMovieToWatchList(
        mediaListItem: MediaListItemModel,
    ) {
        viewModelScope.safeLaunch {
            val itemUpdated = mediaListItem.copy(watchButton = WatchButtonModel.Selected)
            mediaListItemHasChangedThusUpdateHomeMediaListModel(itemUpdated)
            val params = AddMediaToWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
            addMediaToWatchListUseCase(params)
                .onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(mediaListItem) }
        }
    }

    private fun removeMovieFromWatchList(
        mediaListItem: MediaListItemModel,
    ) {
        viewModelScope.safeLaunch {
            val itemUpdated = mediaListItem.copy(watchButton = WatchButtonModel.Unselected)
            mediaListItemHasChangedThusUpdateHomeMediaListModel(itemUpdated)
            val params = RemoveMediaFromWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
            removeMediaFromWatchListUseCase(params)
                .onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(mediaListItem) }
        }
    }

    private fun mediaListItemHasChangedThusUpdateHomeMediaListModel(
        mediaListItem: MediaListItemModel,
    ) {
        _uiState.update { value -> value.copy(model = value.model.updateMedia(mediaListItem)) }
    }
}
