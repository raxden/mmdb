package com.raxdenstudios.app.list.view.viewmodel

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.list.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.list.view.mapper.MediaListModelMapper
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
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
internal class MediaListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

    private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(Media.Movie.empty)
    }
    private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(true)
    }
    private val getMediasUseCase: GetMediasUseCase = mockk {
        coEvery { this@mockk.invoke(aGetMoviesUseCaseFirstPageParams) } returns ResultData.Success(aFirstPageList)
        coEvery { this@mockk.invoke(aGetMoviesUseCaseSecondPageParams) } returns ResultData.Success(aSecondPageList)
    }
    private val paginationConfig = Pagination.Config.default.copy(
        initialPage = aFirstPage,
        pageSize = aPageSize,
        prefetchDistance = 0
    )
    private val getMediasUseCaseParamsMapper = GetMediasUseCaseParamsMapper()
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val mediaListItemModelMapper = MediaListItemModelMapper()
    private val mediaListModelMapper = MediaListModelMapper(
        stringProvider = stringProvider,
        mediaListItemModelMapper = mediaListItemModelMapper,
    )
    private val mediaListParamsFactory: MediaListParamsFactory = mockk(relaxed = true) {
        every { create() } returns MediaListParams.popularMovies
    }
    private val viewModel: MediaListViewModel by lazy {
        MediaListViewModel(
            mediaListParamsFactory = mediaListParamsFactory,
            paginationConfig = paginationConfig,
            getMediasUseCase = getMediasUseCase,
            addMediaToWatchListUseCase = addMediaToWatchListUseCase,
            removeMediaFromWatchListUseCase = removeMediaFromWatchListUseCase,
            getMediasUseCaseParamsMapper = getMediasUseCaseParamsMapper,
            mediaListModelMapper = mediaListModelMapper,
        )
    }

    @Test
    fun `Given a params, When viewModel is instantiated, Then first page of movies is loaded`() = runTest {
        viewModel.uiState.test {
            assertThat(awaitItem()).isEqualTo(MediaListContract.UIState.loading())
            assertThat(awaitItem()).isEqualTo(
                MediaListContract.UIState(
                    model = MediaListModel.empty.copy(
                        items = listOf(
                            MediaListItemModel.empty.copy(id = MediaId(1L)),
                            MediaListItemModel.empty.copy(id = MediaId(2L)),
                        )
                    )
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given a movie, When addMovieToWatchList is called, Then movie is replaced in model`() = runTest {
        val mediaListItemModel = MediaListItemModel.empty.copy(
            id = MediaId(2L),
            watchButton = WatchButtonModel.Unselected,
        )

        viewModel.uiState.test {
            skipItems(2)
            viewModel.setUserEvent(MediaListContract.UserEvent.WatchButtonClicked(mediaListItemModel))
            assertThat(awaitItem()).isEqualTo(
                MediaListContract.UIState(
                    model = MediaListModel.empty.copy(
                        items = listOf(
                            MediaListItemModel.empty.copy(id = MediaId(1L)),
                            MediaListItemModel.empty.copy(
                                id = MediaId(2L),
                                watchButton = WatchButtonModel.Selected
                            ),
                        )
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
                items = listOf(Media.Movie.empty.copy(id = MediaId(1), watchList = true)),
                page = Page(1)
            )
        )
        val mediaListItemModel = MediaListItemModel.empty.copy(
            id = MediaId(1L),
            watchButton = WatchButtonModel.Selected,
        )

        viewModel.uiState.test {
            skipItems(2)
            viewModel.setUserEvent(MediaListContract.UserEvent.WatchButtonClicked(mediaListItemModel))
            assertThat(awaitItem()).isEqualTo(
                MediaListContract.UIState(
                    model = MediaListModel.empty.copy(
                        items = listOf(
                            MediaListItemModel.empty.copy(id = MediaId(1L),
                                watchButton = WatchButtonModel.Unselected
                            ),
                        )
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
                    MediaListContract.UIState(
                        isLoading = true,
                        model = MediaListModel.empty.copy(
                            items = listOf(
                                MediaListItemModel.empty.copy(id = MediaId(1L)),
                                MediaListItemModel.empty.copy(id = MediaId(2L)),
                            )
                        )
                    )
                )
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState(
                        model = MediaListModel.empty.copy(
                            items = listOf(
                                MediaListItemModel.empty.copy(id = MediaId(1L)),
                                MediaListItemModel.empty.copy(id = MediaId(2L)),
                            )
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
                assertThat(awaitItem()).isEqualTo(MediaListContract.UIState.loading())
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState(
                        model = MediaListModel.empty.copy(
                            items = listOf(
                                MediaListItemModel.empty.copy(id = MediaId(1L)),
                                MediaListItemModel.empty.copy(id = MediaId(2L)),
                            )
                        )
                    )
                )
                viewModel.setUserEvent(MediaListContract.UserEvent.LoadMore(pageIndex))
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState(
                        isLoading = true,
                        model = MediaListModel.empty.copy(
                            items = listOf(
                                MediaListItemModel.empty.copy(id = MediaId(1L)),
                                MediaListItemModel.empty.copy(id = MediaId(2L)),
                            )
                        )
                    )
                )
                assertThat(awaitItem()).isEqualTo(
                    MediaListContract.UIState(
                        model = MediaListModel.empty.copy(
                            items = listOf(
                                MediaListItemModel.empty.copy(id = MediaId(1L)),
                                MediaListItemModel.empty.copy(id = MediaId(2L)),
                                MediaListItemModel.empty.copy(id = MediaId(3L)),
                                MediaListItemModel.empty.copy(id = MediaId(4L)),
                            )
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
        private val aGetMoviesUseCaseFirstPageParams =
            GetMediasUseCase.Params(MediaFilter.popularMovies, aFirstPage, aPageSize)
        private val aGetMoviesUseCaseSecondPageParams =
            GetMediasUseCase.Params(MediaFilter.popularMovies, aSecondPage, aPageSize)
        private val aFirstPageMovies = listOf(
            Media.Movie.empty.copy(id = MediaId(1)),
            Media.Movie.empty.copy(id = MediaId(2)),
        )
        private val aSecondPageMovies = listOf(
            Media.Movie.empty.copy(id = MediaId(3)),
            Media.Movie.empty.copy(id = MediaId(4)),
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
