package com.raxdenstudios.app.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.ChangeHomeModuleFilterUseCase
import com.raxdenstudios.app.core.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.home.mapper.CarouselModelToMediaFilterMapper
import com.raxdenstudios.app.feature.home.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.mapFailure
import com.raxdenstudios.commons.ext.onFailure
import com.raxdenstudios.commons.ext.onSuccess
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.provider.StringProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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
    private val errorModelMapper: ErrorModelMapper,
    private val stringProvider: StringProvider,
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeContract.UIState.loading)
    val uiState: StateFlow<HomeContract.UIState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeContract.UIEvent>()
    val uiEvent: SharedFlow<HomeContract.UIEvent> = _uiEvent.asSharedFlow()

    init {
        loadData()
    }

    private fun loadData() = viewModelScope.safeLaunch {
        getHomeModulesUseCase()
            .map { result ->
                result.map { modules -> homeModuleModelMapper.transform(modules) }
                    .mapFailure { error -> errorModelMapper.transform(error) }
            }
            .collect { result ->
                result.onSuccess { media -> loadDataSuccess(media) }
                    .onFailure { error -> loadDataFailure(error) }
            }
    }

    private fun loadDataFailure(error: ErrorModel) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                error = error
            )
        }
    }

    private fun loadDataSuccess(modules: List<HomeModuleModel>) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                modules = modules
            )
        }
    }

    fun setUserEvent(event: HomeContract.UserEvent): Unit = when (event) {
        is HomeContract.UserEvent.MediaSelected -> mediaSelected(event.item)
        is HomeContract.UserEvent.WatchButtonClicked -> when (event.item.watchlist) {
            true -> removeFromWatchlist(event.item)
            false -> addToWatchlist(event.item)
        }

        is HomeContract.UserEvent.SeeAllButtonClicked -> viewAllButtonSelected(event.module)
        is HomeContract.UserEvent.MediaFilterClicked -> filterChanged(event.module, event.filter)
        HomeContract.UserEvent.ErrorDismissed -> errorDismissed()
    }

    private fun viewAllButtonSelected(module: HomeModuleModel.Carousel) {
        val mediaFilter = carouselModelToMediaFilterMapper.transform(module)
        val event = HomeContract.UIEvent.NavigateToMedias(
            mediaFilter.mediaType,
            mediaFilter.mediaCategory
        )
        viewModelScope.safeLaunch { _uiEvent.emit(event) }
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

    private fun mediaSelected(media: MediaModel) {
        val event = HomeContract.UIEvent.NavigateToMedia(media.id, media.mediaType)
        viewModelScope.safeLaunch { _uiEvent.emit(event) }
    }

    private fun addToWatchlist(media: MediaModel) {
        viewModelScope.safeLaunch {
            val params = AddMediaToWatchlistUseCase.Params(
                media.id,
                media.mediaType
            )
            addMediaToWatchlistUseCase(params)
                .onSuccess { mediaAddedToWatchlist(media) }
        }
    }

    private fun mediaAddedToWatchlist(media: MediaModel) {
        val message = stringProvider.getString(R.string.added_to_watchlist, media.title)
        val event = HomeContract.UIEvent.ShowMessage(message)
        viewModelScope.safeLaunch { _uiEvent.emit(event) }
    }

    private fun removeFromWatchlist(media: MediaModel) {
        viewModelScope.safeLaunch {
            val params = RemoveMediaFromWatchlistUseCase.Params(
                media.id,
                media.mediaType
            )
            removeMediaFromWatchlistUseCase(params)
                .onSuccess { mediaRemovedFromWatchlist(media) }
        }
    }

    private fun mediaRemovedFromWatchlist(media: MediaModel) {
        val message = stringProvider.getString(R.string.removed_from_watchlist, media.title)
        val event = HomeContract.UIEvent.ShowMessage(message)
        viewModelScope.safeLaunch { _uiEvent.emit(event) }
    }

    private fun errorDismissed() {
        _uiState.update { value -> value.copy(error = null) }
    }
}
