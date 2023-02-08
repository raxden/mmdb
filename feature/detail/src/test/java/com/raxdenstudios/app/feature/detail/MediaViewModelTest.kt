package com.raxdenstudios.app.feature.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediaUseCase
import com.raxdenstudios.app.core.domain.GetMediaVideosUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.mapper.VideoModelMapper
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.app.feature.detail.model.VideoModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
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
    private val getMediaVideosUseCase: GetMediaVideosUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(videos)
    }
    private val mediaParamsFactory: MediaParamsFactory = mockk {
        coEvery { create() } returns params
    }
    private val mediaModelMapper: MediaModelMapper = mockk {
        every { transform(media) } returns mediaModel
    }
    private val videoModelMapper: VideoModelMapper = mockk {
        every { transform(videos) } returns videosModel
    }
    private val errorModelMapper: ErrorModelMapper = mockk(relaxed = true) {
        every { transform(any<Throwable>()) } returns ErrorModel.mock
        every { transform(any<ErrorDomain>()) } returns ErrorModel.mock
    }
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk(relaxed = true)
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MediaViewModel

    @Before
    fun setUp() {
        viewModel = MediaViewModel(
            getMediaUseCase = getMediaUseCase,
            getMediaVideosUseCase = getMediaVideosUseCase,
            mediaParamsFactory = mediaParamsFactory,
            mediaModelMapper = mediaModelMapper,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaFromWatchlistUseCase,
            videoModelMapper = videoModelMapper,
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
                    videos = listOf(VideoModel.mock),
                )
            )
        }
    }

    @Test
    fun `When viewModel is started, Then videos are loaded`() = runTest {
        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(MediaContract.UIState.loading)

            val uiState2 = awaitItem()
            assertThat(uiState2).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = listOf(VideoModel.mock),
                )
            )
        }
    }

    @Test
    fun `Given an error, When viewModel is started, Then error is loaded`() = runTest {
        coEvery { getMediaUseCase.invoke(any()) } returns flowOf(ResultData.Failure(ErrorDomain.Unknown("")))

        viewModel.uiState.test {
            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(MediaContract.UIState.loading)

            val uiState2 = awaitItem()
            assertThat(uiState2).isEqualTo(
                MediaContract.UIState(
                    videos = listOf(VideoModel.mock),
                    error = ErrorModel.mock,
                )
            )
        }
    }

    @Test
    fun `When backClicked is received, Then update uiState`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(MediaContract.UserEvent.BackClicked)

            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = listOf(VideoModel.mock),
                    events = setOf(MediaContract.UIEvent.NavigateToBack),
                )
            )
        }
    }

    @Test
    fun `When addToWatchlistClicked is received, Then update uiState`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(MediaContract.UserEvent.AddToWatchlist(mediaModel))

            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = listOf(VideoModel.mock),
                )
            )
            coVerify(exactly = 1) { addMediaToWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When removeFromWatchlistClicked is received, Then update uiState`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(MediaContract.UserEvent.RemoveFromWatchlist(mediaModel))

            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = listOf(VideoModel.mock),
                )
            )
            coVerify(exactly = 1) { removeMediaFromWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When videoClicked is received, Then update uiState`() = runTest {
        viewModel.uiState.test {
            skipItems(2)

            viewModel.setUserEvent(MediaContract.UserEvent.VideoClick(videoModel))

            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = listOf(VideoModel.mock),
                    events = setOf(MediaContract.UIEvent.PlayYoutubeVideo("https://www.youtube.com/watch?v=l6rAoph5UgI")),
                )
            )
        }
    }

    @Test
    fun `When error is dismissed, Then update uiState`() = runTest {
        coEvery { getMediaVideosUseCase.invoke(any()) } returns ResultData.Failure(ErrorDomain.Unknown(""))

        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(MediaContract.UserEvent.ErrorDismissed)

            val uiState = awaitItem()
            assertThat(uiState).isEqualTo(
                MediaContract.UIState(
                    media = MediaModel.mock,
                    videos = emptyList(),
                    error = null,
                )
            )
        }
    }

    companion object {

        private val params = MediaParams(
            mediaId = MediaId(1),
            mediaType = MediaType.Movie
        )
        private val media = Media.Movie.mock
        private val mediaModel = MediaModel.mock
        private val videoModel = VideoModel.mock
        private val videos = listOf(Video.mock)
        private val videosModel = listOf(VideoModel.mock)
    }
}
