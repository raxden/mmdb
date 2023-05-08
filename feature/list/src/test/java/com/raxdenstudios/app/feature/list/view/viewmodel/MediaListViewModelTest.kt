package com.raxdenstudios.app.feature.list.view.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.GetMediasUseCase
import com.raxdenstudios.app.core.domain.GetRelatedMediasUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.CurrencyModelMapper
import com.raxdenstudios.app.core.ui.mapper.DateModelMapper
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.LanguageModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.mapper.PictureModelMapper
import com.raxdenstudios.app.core.ui.mapper.RatingModelMapper
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.MediaListContract
import com.raxdenstudios.app.feature.MediaListParamsFactory
import com.raxdenstudios.app.feature.MediaListViewModel
import com.raxdenstudios.app.feature.mapper.MediaListTitleModelMapper
import com.raxdenstudios.app.feature.model.MediaListParams
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk()
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(true)
    }
    private val getRelatedMediasUseCase: GetRelatedMediasUseCase = mockk()
    private val getMediasUseCase: GetMediasUseCase = mockk {
        coEvery { this@mockk.invoke(aGetMoviesUseCaseFirstPageParams) } returns ResultData.Success(aFirstPageList)
        coEvery { this@mockk.invoke(aGetMoviesUseCaseSecondPageParams) } returns ResultData.Success(aSecondPageList)
    }
    private val paginationConfig = Pagination.Config.default.copy(
        initialPage = aFirstPage,
        pageSize = aPageSize,
        prefetchDistance = 0
    )
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val durationModelMapper = DurationModelMapper(
        stringProvider = stringProvider
    )
    private val dateModelMapper = DateModelMapper()
    private val languageModelMapper = LanguageModelMapper()
    private val currencyModelMapper = CurrencyModelMapper()
    private val ratingModelMapper = RatingModelMapper()
    private val pictureModelMapper = PictureModelMapper()
    private val mediaModelMapper = MediaModelMapper(
        durationModelMapper = durationModelMapper,
        dateModelMapper = dateModelMapper,
        languageModelMapper = languageModelMapper,
        currencyModelMapper = currencyModelMapper,
        ratingModelMapper = ratingModelMapper,
        pictureModelMapper = pictureModelMapper,
    )
    private val mediaListTitleModelMapper = MediaListTitleModelMapper(
        stringProvider = stringProvider,
    )
    private val errorModelMapper: ErrorModelMapper = mockk(relaxed = true)
    private val mediaListParamsFactory: MediaListParamsFactory = mockk(relaxed = true) {
        every { create() } returns MediaListParams.List(MediaFilter.popular(MediaType.Movie))
    }
    private val viewModel: MediaListViewModel by lazy {
        MediaListViewModel(
            mediaListParamsFactory = mediaListParamsFactory,
            paginationConfig = paginationConfig,
            getMediasUseCase = getMediasUseCase,
            getRelatedMediasUseCase = getRelatedMediasUseCase,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaFromWatchlistUseCase,
            mediaListTitleModelMapper = mediaListTitleModelMapper,
            mediaModelMapper = mediaModelMapper,
            errorModelMapper = errorModelMapper,
        )
    }

    @Test
    fun `Given a params, When viewModel is instantiated, Then first page of movies is loaded`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MediaListContract.UIState.loading())
            assertThat(awaitItem()).isEqualTo(
                MediaListContract.UIState.empty.copy(
                    items = listOf(
                        MediaModel.mock.copy(id = MediaId(1L)),
                        MediaModel.mock.copy(id = MediaId(2L)),
                    )
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given a movie, When addMovieToWatchlist is called, Then movie is replaced in model`() = runTest {
        val mediaModel = MediaModel.mock.copy(
            id = MediaId(2L),
            watchlist = false,
        )
        coEvery { addMediaToWatchlistUseCase.invoke(any()) } returns ResultData.Success(
            Media.Movie.mock.copy(id = MediaId(2L), watchList = true)
        )

        viewModel.uiState.test {
            skipItems(2)

            viewModel.setUserEvent(MediaListContract.UserEvent.WatchButtonClicked(mediaModel))

            val result = awaitItem()
            assertThat(result).isEqualTo(
                MediaListContract.UIState.empty.copy(
                    items = listOf(
                        MediaModel.mock.copy(id = MediaId(1L)),
                        MediaModel.mock.copy(
                            id = MediaId(2L),
                            watchlist = true,
                        ),
                    )
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when movie is removed from watchlist, movie is replaced in model`() = runTest {
        coEvery { getMediasUseCase.invoke(any()) } returns ResultData.Success(
            PageList(
                items = listOf(Media.Movie.mock.copy(id = MediaId(1), watchList = true)),
                page = Page(1)
            )
        )
        val mediaModel = MediaModel.mock.copy(
            id = MediaId(1L),
            watchlist = true,
        )

        viewModel.uiState.test {
            skipItems(2)
            viewModel.setUserEvent(MediaListContract.UserEvent.WatchButtonClicked(mediaModel))
            assertThat(awaitItem()).isEqualTo(
                MediaListContract.UIState.empty.copy(
                    items = listOf(
                        MediaModel.mock.copy(
                            id = MediaId(1L),
                            watchlist = false,
                        ),
                    )
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given a params with searchType as popular, When refreshMovies method is called, Then first page with movies is returned`() =
        runTest {
            viewModel.uiState.test {
                skipItems(2)
                viewModel.setUserEvent(MediaListContract.UserEvent.Refresh)
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState.empty.copy(
                        isLoading = true,
                        items = listOf(
                            MediaModel.mock.copy(id = MediaId(1L)),
                            MediaModel.mock.copy(id = MediaId(2L)),
                        )
                    )
                )
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState.empty.copy(
                        items = listOf(
                            MediaModel.mock.copy(id = MediaId(1L)),
                            MediaModel.mock.copy(id = MediaId(2L)),
                        )
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `Given a pageIndex with value 20, When loadMoreMovies is called, Then second page with movies are returned`() =
        runTest {
            val pageIndex = PageIndex(2)

            viewModel.uiState.test {
                val item1 = awaitItem()
                assertThat(item1).isEqualTo(MediaListContract.UIState.loading())
                val item2 = awaitItem()
                assertThat(item2).isEqualTo(
                    MediaListContract.UIState.empty.copy(
                        items = listOf(
                            MediaModel.mock.copy(id = MediaId(1L)),
                            MediaModel.mock.copy(id = MediaId(2L)),
                        )
                    )
                )
                viewModel.setUserEvent(MediaListContract.UserEvent.LoadMore(pageIndex))
                val item3 = awaitItem()
                assertThat(item3).isEqualTo(
                    MediaListContract.UIState.empty.copy(
                        items = listOf(
                            MediaModel.mock.copy(id = MediaId(1L)),
                            MediaModel.mock.copy(id = MediaId(2L)),
                            MediaModel.mock.copy(id = MediaId(3L)),
                            MediaModel.mock.copy(id = MediaId(4L)),
                        )
                    )
                )
                cancelAndIgnoreRemainingEvents()
            }
        }

    companion object {

        private val aFirstPage = Page(1)
        private val aSecondPage = Page(2)
        private val aPageSize = PageSize(2)
        private val aMediaFilter = MediaFilter.popular(MediaType.Movie)
        private val aGetMoviesUseCaseFirstPageParams =
            GetMediasUseCase.Params(aMediaFilter, aFirstPage, aPageSize)
        private val aGetMoviesUseCaseSecondPageParams =
            GetMediasUseCase.Params(aMediaFilter, aSecondPage, aPageSize)
        private val aFirstPageMovies = listOf(
            Media.Movie.mock.copy(id = MediaId(1)),
            Media.Movie.mock.copy(id = MediaId(2)),
        )
        private val aSecondPageMovies = listOf(
            Media.Movie.mock.copy(id = MediaId(3)),
            Media.Movie.mock.copy(id = MediaId(4)),
        )
        private val aFirstPageList = PageList<Media>(
            items = aFirstPageMovies,
            aFirstPage,
        )
        private val aSecondPageList = PageList<Media>(
            items = aSecondPageMovies,
            aSecondPage,
        )
    }
}
