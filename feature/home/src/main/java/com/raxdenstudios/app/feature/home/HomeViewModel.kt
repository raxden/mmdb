package com.raxdenstudios.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.ChangeHomeModuleFilterUseCase
import com.raxdenstudios.app.core.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.home.mapper.CarouselModelToMediaFilterMapper
import com.raxdenstudios.app.feature.home.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.ext.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeModulesUseCase: GetHomeModulesUseCase,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase,
    private val homeModuleModelMapper: HomeModuleModelMapper,
    private val changeHomeModuleFilterUseCase: ChangeHomeModuleFilterUseCase,
    private val carouselModelToMediaFilterMapper: CarouselModelToMediaFilterMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.UIState.loading)
    val uiState: StateFlow<HomeContract.UIState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.safeLaunch {
        getHomeModulesUseCase()
            .map { modules -> homeModuleModelMapper.transform(modules) }
            .collect { modules ->
                _uiState.update { value ->
                    value.copy(
                        isLoading = false,
                        modules = modules
                    )
                }
            }
    }

    fun setUserEvent(event: HomeContract.UserEvent): Unit = when (event) {
        is HomeContract.UserEvent.MediaSelected -> mediaSelected(
            event.mediaItemModel.id,
            event.mediaItemModel.mediaType
        )
        is HomeContract.UserEvent.WatchButtonClicked -> when (event.item.watchlist) {
            true -> removeFromWatchlist(event.item)
            false -> addToWatchlist(event.item)
        }
        is HomeContract.UserEvent.SeeAllButtonClicked -> viewAllButtonSelected(event.module)
        is HomeContract.UserEvent.MediaFilterClicked -> filterChanged(event.module, event.filter)
    }

    private fun viewAllButtonSelected(module: HomeModuleModel.Carousel) {
        _uiState.update { value ->
            val mediaFilter = carouselModelToMediaFilterMapper.transform(module)
            val event = HomeContract.UIEvent.NavigateToMedias(
                mediaFilter.mediaType,
                mediaFilter.mediaCategory
            )
            value.copy(events = value.events.plus(event))
        }
    }

    fun eventConsumed(event: HomeContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }

    private fun filterChanged(
        module: HomeModuleModel.Carousel,
        filter: MediaFilterModel,
    ) {
        viewModelScope.safeLaunch {
            val params = ChangeHomeModuleFilterUseCase.Params(module.id, filter.id)
            changeHomeModuleFilterUseCase(params)
        }
    }

    private fun mediaSelected(
        id: MediaId,
        mediaType: MediaType
    ) {
        _uiState.update { value ->
            val event = HomeContract.UIEvent.NavigateToMedia(id, mediaType)
            value.copy(events = value.events.plus(event))
        }
    }

    private fun addToWatchlist(
        mediaListItem: MediaModel,
    ) {
        viewModelScope.safeLaunch {
            addMediaToWatchlistUseCase(
                params = AddMediaToWatchlistUseCase.Params(
                    mediaListItem.id,
                    mediaListItem.mediaType
                )
            )
        }
    }

    private fun removeFromWatchlist(
        mediaListItem: MediaModel,
    ) {
        viewModelScope.safeLaunch {
            removeMediaFromWatchlistUseCase(
                params = RemoveMediaFromWatchlistUseCase.Params(
                    mediaListItem.id,
                    mediaListItem.mediaType
                )
            )
        }
    }
}
