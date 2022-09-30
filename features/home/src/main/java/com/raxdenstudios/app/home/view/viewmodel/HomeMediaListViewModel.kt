package com.raxdenstudios.app.home.view.viewmodel

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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import java.util.UUID
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

    private val _state = MutableStateFlow(UIState.loading())
    val state: StateFlow<UIState> = _state.asStateFlow()

    init {
        loadData()
    }

    fun loadData() = viewModelScope.safeLaunch {
        _state.value = UIState.loading()

        getHomeModulesUseCase()
            .catch { error -> _state.update { value -> value.copy(error = error) } }
            .map { modules -> homeModelMapper.transform(modules) }
            .collect { model -> _state.update { value -> value.copy(loading = false, model = model) } }
    }

    fun mediaSelected(
        model: HomeMediaListModel,
        carouselMedias: HomeModuleModel.CarouselMedias,
        mediaItemModel: MediaListItemModel,
    ) {
        Unit
    }

    fun viewAllButtonSelected(carouselMedias: HomeModuleModel.CarouselMedias) {
        _state.update { value -> value.copy(events = value.events.plus(UIEvent.NavigateToMediaList(carouselMedias))) }
    }

    fun eventConsumed(event: UIEvent) {
        _state.update { value -> value.copy(events = value.events.minus(event)) }
    }

    fun filterChanged(
        model: HomeMediaListModel,
        carouselMedias: HomeModuleModel.CarouselMedias,
        mediaType: MediaType,
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
        _state.update { value -> value.copy(model = homeUpdated) }
    }

    fun watchListPressed(
        home: HomeMediaListModel,
        mediaListItem: MediaListItemModel,
    ) {
        when (mediaListItem.watchButton) {
            is WatchButtonModel.Selected -> {
                viewModelScope.safeLaunch {
                    val itemUpdated = mediaListItem.copy(watchButton = WatchButtonModel.Unselected)
                    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
                    val params = RemoveMediaFromWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
                    removeMediaFromWatchListUseCase(params)
                        .onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
                }
            }
            is WatchButtonModel.Unselected -> {
                viewModelScope.safeLaunch {
                    val itemUpdated = mediaListItem.copy(watchButton = WatchButtonModel.Selected)
                    mediaListItemHasChangedThusUpdateHomeMediaListModel(home, itemUpdated)
                    val params = AddMediaToWatchListUseCase.Params(mediaListItem.id, mediaListItem.mediaType)
                    addMediaToWatchListUseCase(params)
                        .onFailure { mediaListItemHasChangedThusUpdateHomeMediaListModel(home, mediaListItem) }
                }
            }
        }
    }

    private fun mediaListItemHasChangedThusUpdateHomeMediaListModel(
        home: HomeMediaListModel,
        mediaListItem: MediaListItemModel,
    ) {
        val homeUpdated = home.updateMedia(mediaListItem)
        _state.update { value -> value.copy(model = homeUpdated) }
    }

    internal data class UIState(
        val loading: Boolean,
        val model: HomeMediaListModel,
        val events: List<UIEvent>,
        val error: Throwable?,
    ) {

        companion object {

            fun loading() = UIState(
                loading = true,
                model = HomeMediaListModel.empty,
                events = emptyList(),
                error = null,
            )
        }
    }

    /**
     * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
     *
     * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
     */
    internal sealed class UIEvent {
        val id: String = UUID.randomUUID().toString()

        data class NavigateToMediaList(
            val carouselMedias: HomeModuleModel.CarouselMedias,
        ) : UIEvent()
    }
}
