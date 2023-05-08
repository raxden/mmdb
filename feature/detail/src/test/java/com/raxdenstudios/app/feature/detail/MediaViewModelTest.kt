package com.raxdenstudios.app.feature.detail

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediaUseCase
import com.raxdenstudios.app.core.domain.GetMediaVideosUseCase
import com.raxdenstudios.app.core.domain.GetRelatedMediasUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.ui.mapper.CurrencyModelMapper
import com.raxdenstudios.app.core.ui.mapper.DateModelMapper
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.LanguageModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.mapper.PictureModelMapper
import com.raxdenstudios.app.core.ui.mapper.RatingModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.detail.mapper.MediaPageListResultModelMapper
import com.raxdenstudios.app.feature.detail.mapper.MediaResultModelMapper
import com.raxdenstudios.app.feature.detail.mapper.VideoModelMapper
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.app.feature.detail.model.VideoModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
    private val getRelatedMediasUseCase: GetRelatedMediasUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns flow { emit(ResultData.Success(mediasPaged)) }
    }
    private val getMediaVideosUseCase: GetMediaVideosUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(videos)
    }
    private val mediaParamsFactory: MediaParamsFactory = mockk {
        coEvery { create() } returns params
    }
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val durationModelMapper: DurationModelMapper = DurationModelMapper(stringProvider)
    private val videoModelMapper: VideoModelMapper = VideoModelMapper()
    private val dateModelMapper = DateModelMapper()
    private val languageModelMapper = LanguageModelMapper()
    private val currencyModelMapper = CurrencyModelMapper()
    private val ratingModelMapper = RatingModelMapper()
    private val pictureModelMapper = PictureModelMapper()
    private val mediaModelMapper: MediaModelMapper = MediaModelMapper(
        durationModelMapper = durationModelMapper,
        dateModelMapper = dateModelMapper,
        languageModelMapper = languageModelMapper,
        currencyModelMapper = currencyModelMapper,
        ratingModelMapper = ratingModelMapper,
        pictureModelMapper = pictureModelMapper,
    )
    private val errorModelMapper: ErrorModelMapper = ErrorModelMapper(stringProvider)
    private val mediaResultModelMapper: MediaResultModelMapper = MediaResultModelMapper(
        mediaModelMapper = mediaModelMapper,
        errorModelMapper = errorModelMapper,
    )
    private val mediaPageListResultModelMapper: MediaPageListResultModelMapper = MediaPageListResultModelMapper(
        mediaModelMapper = mediaModelMapper,
        errorModelMapper = errorModelMapper,
        stringProvider = stringProvider,
    )
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk(relaxed = true)
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk(relaxed = true)
    private lateinit var viewModel: MediaViewModel

    @Before
    fun setUp() {
        viewModel = MediaViewModel(
            getMediaUseCase = getMediaUseCase,
            getMediaVideosUseCase = getMediaVideosUseCase,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaFromWatchlistUseCase,
            getRelatedMediasUseCase = getRelatedMediasUseCase,
            mediaParamsFactory = mediaParamsFactory,
            mediaResultModelMapper = mediaResultModelMapper,
            mediaPageListResultModelMapper = mediaPageListResultModelMapper,
            videoModelMapper = videoModelMapper,
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
                    relatedMedias = RelatedMediasModel.mock,
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
                    relatedMedias = RelatedMediasModel.mock,
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
                    relatedMedias = RelatedMediasModel.mock,
                )
            )
        }
    }

    @Test
    fun `When backClicked is received, Then update uiState`() = runTest {
        viewModel.uiEvent.test {

            viewModel.setUserEvent(MediaContract.UserEvent.BackClicked)

            val uiEvent = awaitItem()
            assertThat(uiEvent).isEqualTo(MediaContract.UIEvent.NavigateToBack)
        }
    }

    @Test
    fun `When addToWatchlistClicked is received, Then update uiState`() = runTest {
        val media = MediaModel.mock.copy(watchlist = false)

        viewModel.uiState.test {
            viewModel.setUserEvent(MediaContract.UserEvent.WatchlistClick(media))

            cancelAndConsumeRemainingEvents()
            coVerify(exactly = 1) { addMediaToWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When removeFromWatchlistClicked is received, Then update uiState`() = runTest {
        val media = MediaModel.mock.copy(watchlist = true)

        viewModel.uiState.test {
            viewModel.setUserEvent(MediaContract.UserEvent.WatchlistClick(media))

            cancelAndConsumeRemainingEvents()
            coVerify(exactly = 1) { removeMediaFromWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When videoClicked is received, Then update uiState`() = runTest {
        viewModel.uiEvent.test {

            viewModel.setUserEvent(MediaContract.UserEvent.VideoClick(videoModel))

            val uiEvent = awaitItem()
            assertThat(uiEvent).isEqualTo(MediaContract.UIEvent.PlayYoutubeVideo("https://www.youtube.com/watch?v=l6rAoph5UgI"))
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
                    relatedMedias = RelatedMediasModel.mock,
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
        private val videoModel = VideoModel.mock
        private val videos = listOf(Video.mock)
        private val mediasPaged = PageList<Media>(
            items = listOf(Media.Movie.mock),
            page = Page(1),
        )
    }
}
