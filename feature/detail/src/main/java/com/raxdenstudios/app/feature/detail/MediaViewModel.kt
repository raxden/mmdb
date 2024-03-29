package com.raxdenstudios.app.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediaUseCase
import com.raxdenstudios.app.core.domain.GetMediaVideosUseCase
import com.raxdenstudios.app.core.domain.GetRelatedMediasUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.mapper.MediaPageListResultModelMapper
import com.raxdenstudios.app.feature.detail.mapper.MediaResultModelMapper
import com.raxdenstudios.app.feature.detail.mapper.VideoModelMapper
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.app.feature.detail.model.VideoModel
import com.raxdenstudios.commons.core.ext.fold
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.onSuccess
import com.raxdenstudios.commons.coroutines.ext.safeLaunch
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

@Suppress("TooManyFunctions")
@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase,
    private val getMediaVideosUseCase: GetMediaVideosUseCase,
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase,
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase,
    private val getRelatedMediasUseCase: GetRelatedMediasUseCase,
    private val mediaParamsFactory: MediaParamsFactory,
    private val mediaResultModelMapper: MediaResultModelMapper,
    private val mediaPageListResultModelMapper: MediaPageListResultModelMapper,
    private val videoModelMapper: VideoModelMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaContract.UIState.loading)
    val uiState: StateFlow<MediaContract.UIState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<MediaContract.UIEvent>()
    val uiEvent: SharedFlow<MediaContract.UIEvent> = _uiEvent.asSharedFlow()

    private val params: MediaParams by lazy { mediaParamsFactory.create() }

    init {
        loadMedia()
        loadMediaVideos()
        loadRelatedMedias()
    }

    fun setUserEvent(event: MediaContract.UserEvent): Unit = when (event) {
        MediaContract.UserEvent.BackClicked -> backClicked()
        MediaContract.UserEvent.ErrorDismissed -> errorDismissed()
        is MediaContract.UserEvent.VideoClick -> videoClick(event.video)
        is MediaContract.UserEvent.RelatedMediaClick -> relatedMediaClick(event.media)
        is MediaContract.UserEvent.RelatedWatchlistClick -> watchlistClick(event.media)
        is MediaContract.UserEvent.WatchlistClick -> watchlistClick(event.media)
        MediaContract.UserEvent.RelatedSeeAllButtonClicked -> relatedSeeAllClick()
    }

    private fun loadMedia() = viewModelScope.safeLaunch {
        val params = GetMediaUseCase.Params(
            mediaId = params.mediaId,
            mediaType = params.mediaType,
        )
        getMediaUseCase(params)
            .map { result -> mediaResultModelMapper.transform(result) }
            .collect { result -> result.fold(::loadDataSuccess, ::loadDataFailure) }
    }

    private fun loadMediaVideos() {
        val params = GetMediaVideosUseCase.Params(
            mediaId = params.mediaId,
            mediaType = params.mediaType,
        )
        viewModelScope.safeLaunch {
            getMediaVideosUseCase(params)
                .map { videos -> videoModelMapper.transform(videos) }
                .onSuccess { videos -> loadMediaVideosSuccess(videos) }
        }
    }

    private fun loadMediaVideosSuccess(videos: List<VideoModel>) {
        _uiState.update { value -> value.copy(videos = videos) }
    }

    private fun loadRelatedMedias() {
        val params = GetRelatedMediasUseCase.Params(
            mediaId = params.mediaId,
            mediaType = params.mediaType,
        )
        viewModelScope.safeLaunch {
            getRelatedMediasUseCase(params)
                .map { result -> mediaPageListResultModelMapper.transform(result) }
                .collect { result -> result.onSuccess(::loadRelatedMediasSuccess) }
        }
    }

    private fun loadRelatedMediasSuccess(relatedMedias: RelatedMediasModel) {
        _uiState.update { value -> value.copy(relatedMedias = relatedMedias) }
    }

    private fun loadDataFailure(error: ErrorModel) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                error = error
            )
        }
    }

    private fun loadDataSuccess(model: MediaModel) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                media = model
            )
        }
    }

    private fun relatedSeeAllClick() {
        val event = MediaContract.UIEvent.NavigateToRelatedMedias(params.mediaId, params.mediaType)
        viewModelScope.safeLaunch {
            _uiEvent.emit(event)
        }
    }

    private fun relatedMediaClick(media: MediaModel) {
        val event = MediaContract.UIEvent.NavigateToMedia(media.id, media.mediaType)
        viewModelScope.safeLaunch {
            _uiEvent.emit(event)
        }
    }

    private fun watchlistClick(media: MediaModel) {
        if (media.watchlist) {
            removeFromWatchlist(media)
        } else {
            addToWatchlist(media)
        }
    }

    private fun addToWatchlist(
        media: MediaModel,
    ) {
        viewModelScope.safeLaunch {
            addMediaToWatchlistUseCase(
                params = AddMediaToWatchlistUseCase.Params(
                    media.id,
                    media.mediaType
                )
            )
        }
    }

    private fun removeFromWatchlist(
        media: MediaModel,
    ) {
        viewModelScope.safeLaunch {
            removeMediaFromWatchlistUseCase(
                params = RemoveMediaFromWatchlistUseCase.Params(
                    media.id,
                    media.mediaType
                )
            )
        }
    }

    private fun backClicked() {
        val event = MediaContract.UIEvent.NavigateToBack
        viewModelScope.safeLaunch {
            _uiEvent.emit(event)
        }
    }

    private fun videoClick(video: VideoModel) {
        val event = MediaContract.UIEvent.PlayYoutubeVideo(video.uri)
        viewModelScope.safeLaunch {
            _uiEvent.emit(event)
        }
    }

    private fun errorDismissed() {
        _uiState.update { value -> value.copy(error = null) }
    }
}
