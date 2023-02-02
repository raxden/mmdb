package com.raxdenstudios.app.feature.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediaUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MediaViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

    private val getMediaUseCase: GetMediaUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns flowOf(ResultData.Success(media))
    }
    private val mediaParamsFactory: MediaParamsFactory = mockk {
        coEvery { create() } returns params
    }
    private val mediaModelMapper: MediaModelMapper = mockk {
        every { transform(media) } returns mediaModel
    }
    private val errorModelMapper: ErrorModelMapper = mockk(relaxed = true) {
        every { transform(any<Throwable>()) } returns ErrorModel.mock
    }
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk(relaxed = true)
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MediaViewModel

    @Before
    fun setUp() {
        viewModel = MediaViewModel(
            getMediaUseCase = getMediaUseCase,
            mediaParamsFactory = mediaParamsFactory,
            mediaModelMapper = mediaModelMapper,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaFromWatchlistUseCase,
            errorModelMapper = errorModelMapper,
        )
    }

    @Test
    fun `When viewModel is started, Then media is loaded`() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(MediaContract.UIState.loading)

            val uiState2 = awaitItem()
            assertThat(uiState2).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                )
            )
        }
    }

    @Test
    fun `Given an error, When viewModel is started, Then error is loaded`() = runTest {
        coEvery { getMediaUseCase.invoke(any()) } returns flowOf(ResultData.Error(IllegalStateException()))

        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(MediaContract.UIState.loading)

            val uiState2 = awaitItem()
            assertThat(uiState2).isEqualTo(
                MediaContract.UIState(
                    isLoading = false,
                    error = ErrorModel.mock,
                )
            )
        }
    }

    @Test
    fun `When backClicked is received, Then update uiState`() = runTest {
        viewModel.uiState.test {
            skipItems(2)

            viewModel.setUserEvent(MediaContract.UserEvent.BackClicked)
            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    events = setOf(MediaContract.UIEvent.NavigateToBack),
                )
            )
        }
    }

    companion object {

        private val params = MediaParams(
            mediaId = MediaId(1),
            mediaType = MediaType.Movie
        )
        private val media = Media.Movie.empty
        private val mediaModel = MediaModel.mock
    }
}
