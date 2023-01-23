package com.raxdenstudios.app.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.core.domain.GetMediaUseCase
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.onFailure
import com.raxdenstudios.commons.ext.onSuccess
import com.raxdenstudios.commons.ext.safeLaunch
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MediaViewModel @Inject constructor(
    private val getMediaUseCase: GetMediaUseCase,
    private val mediaParamsFactory: MediaParamsFactory,
    private val mediaModelMapper: MediaModelMapper,
) : ViewModel() {

    private val _uiState = MutableStateFlow(MediaContract.UIState.loading)
    val uiState: StateFlow<MediaContract.UIState> = _uiState.asStateFlow()

    private val params: MediaParams by lazy { mediaParamsFactory.create() }

    init {
        loadData()
    }

    fun setUserEvent(event: MediaContract.UserEvent): Unit = when (event) {
        MediaContract.UserEvent.BackClicked -> updateUIStateWithEvent(MediaContract.UIEvent.NavigateToBack)
    }

    fun eventConsumed(event: MediaContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.minus(event)) }
    }

    private fun updateUIStateWithEvent(event: MediaContract.UIEvent) {
        _uiState.update { value -> value.copy(events = value.events.plus(event)) }
    }

    private fun loadData() = viewModelScope.safeLaunch {
        val params = GetMediaUseCase.Params(
            mediaId = params.mediaId,
            mediaType = params.mediaType,
        )
        getMediaUseCase(params)
            .map { media -> mediaModelMapper.transform(media) }
            .onSuccess { model -> loadDataSuccess(model) }
            .onFailure { error -> loadDataFailure(error) }
    }

    private fun loadDataFailure(error: Throwable) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                error = error
            )
        }
        Timber.e(error)
    }

    private fun loadDataSuccess(model: MediaModel) {
        _uiState.update { value ->
            value.copy(
                isLoading = false,
                media = model
            )
        }
    }
}
