package com.raxdenstudios.app.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.ChangeHomeModuleFilterUseCase
import com.raxdenstudios.app.core.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.home.mapper.CarouselModelMapper
import com.raxdenstudios.app.feature.home.mapper.CarouselModelToMediaFilterMapper
import com.raxdenstudios.app.feature.home.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val getHomeModulesUseCase: GetHomeModulesUseCase = mockk {
        coEvery { this@mockk.invoke() } returns flow { emit(ResultData.Success(modules)) }
    }
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk(relaxed = true)
    private val removeMediaToWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk(relaxed = true)
    private val carouselModelToMediaFilterMapper = CarouselModelToMediaFilterMapper()
    private val durationModelMapper = DurationModelMapper(
        stringProvider = stringProvider
    )
    private val mediaModelMapper = MediaModelMapper(
        durationModelMapper = durationModelMapper,
    )
    private val carouselModelMapper = CarouselModelMapper(
        stringProvider = stringProvider,
        mediaModelMapper = mediaModelMapper,
    )
    private val homeModuleModelMapper = HomeModuleModelMapper(
        carouselModelMapper = carouselModelMapper
    )
    private val errorModelMapper = ErrorModelMapper(
        stringProvider = stringProvider
    )
    private val changeHomeModuleFilterUseCase: ChangeHomeModuleFilterUseCase = mockk()
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        viewModel = HomeViewModel(
            getHomeModulesUseCase = getHomeModulesUseCase,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaToWatchlistUseCase,
            homeModuleModelMapper = homeModuleModelMapper,
            changeHomeModuleFilterUseCase = changeHomeModuleFilterUseCase,
            carouselModelToMediaFilterMapper = carouselModelToMediaFilterMapper,
            errorModelMapper = errorModelMapper,
            stringProvider = stringProvider,
        )
    }

    @Test
    fun `When viewModel is started, Then modules with movies are loaded`() {
        runTest {
            viewModel.uiState.test {
                val uiState = awaitItem()
                assertThat(uiState).isEqualTo(HomeContract.UIState.loading)

                val uiState2 = awaitItem()
                assertThat(uiState2).isEqualTo(
                    HomeContract.UIState(
                        modules = listOf(
                            HomeModuleModel.Carousel.Popular.empty.copy(
                                medias = listOf(
                                    MediaModel.mock.copy(id = MediaId(1L)),
                                    MediaModel.mock.copy(id = MediaId(2L)),
                                ),
                                filters = listOf(
                                    MediaFilterModel(
                                        id = MediaType.Movie,
                                        label = "",
                                        isSelected = true,
                                    ),
                                    MediaFilterModel(
                                        id = MediaType.TvShow,
                                        label = "",
                                        isSelected = false,
                                    ),
                                )
                            ),
                        ),
                    )
                )
            }
        }
    }

    @Test
    fun `When media is selected, Then uiState is updated`() {
        runTest {
            viewModel.uiState.test {
                skipItems(2)
                viewModel.setUserEvent(HomeContract.UserEvent.MediaSelected(MediaModel.mock))

                val uiState = awaitItem()
                assertThat(uiState.events).isEqualTo(
                    setOf(
                        HomeContract.UIEvent.NavigateToMedia(
                            mediaId = MediaId(1L),
                            mediaType = MediaType.Movie,
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `When watchButton is selected, Then add to watchlist`() {
        runTest {
            coEvery { addMediaToWatchlistUseCase.invoke(any()) } returns ResultData.Success(Media.Movie.mock)

            viewModel.setUserEvent(HomeContract.UserEvent.WatchButtonClicked(MediaModel.mock))

            advanceUntilIdle()
            coVerify { addMediaToWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When watchButton is unselected, Then remote from watchlist`() {
        runTest {
            coEvery { removeMediaToWatchlistUseCase(any()) } returns ResultData.Success(true)

            viewModel.setUserEvent(
                HomeContract.UserEvent.WatchButtonClicked(MediaModel.mock.copy(watchlist = true))
            )

            advanceUntilIdle()
            coVerify { removeMediaToWatchlistUseCase(any()) }
        }
    }

    @Test
    fun `When seeAll is selected, Then uiState is updated`() {
        runTest {
            viewModel.uiState.test {
                skipItems(2)
                viewModel.setUserEvent(
                    HomeContract.UserEvent.SeeAllButtonClicked(HomeModuleModel.Carousel.Popular.empty)
                )

                val uiState = awaitItem()
                assertThat(uiState.events).isEqualTo(
                    setOf(
                        HomeContract.UIEvent.NavigateToMedias(
                            mediaType = MediaType.Movie,
                            mediaCategory = MediaCategory.Popular,
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `When filter is selected, Then uiState is updated`() {
        runTest {
            coEvery { changeHomeModuleFilterUseCase.invoke(any()) } returns ResultData.Success(true)
            viewModel.setUserEvent(
                HomeContract.UserEvent.MediaFilterClicked(
                    module = HomeModuleModel.Carousel.Popular.empty,
                    filter = MediaFilterModel.mockk
                )
            )

            advanceUntilIdle()
            coVerify { changeHomeModuleFilterUseCase.invoke(any()) }
        }
    }

    companion object {

        private val medias = listOf(
            Media.Movie.mock.copy(id = MediaId(1)),
            Media.Movie.mock.copy(id = MediaId(2)),
        )
        private val modules: List<HomeModule> =
            listOf(
                HomeModule.Carousel.popular(0, 0, MediaType.Movie).copy(
                    medias = medias
                ),
            )
    }
}
