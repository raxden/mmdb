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
import com.raxdenstudios.app.core.ui.mapper.CurrencyModelMapper
import com.raxdenstudios.app.core.ui.mapper.DateModelMapper
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.LanguageModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.mapper.PictureModelMapper
import com.raxdenstudios.app.core.ui.mapper.RatingModelMapper
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.home.mapper.CarouselModelMapper
import com.raxdenstudios.app.feature.home.mapper.CarouselModelToMediaFilterMapper
import com.raxdenstudios.app.feature.home.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.android.provider.StringProvider
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import net.lachlanmckee.timberjunit.TimberTestRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    @get:Rule
    val timberTestRule: TimberTestRule = TimberTestRule.logAllWhenTestFails()

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val getHomeModulesUseCase: GetHomeModulesUseCase = mockk {
        coEvery { this@mockk.invoke() } returns flow { emit(ResultData.Success(modules)) }
    }
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk(relaxed = true)
    private val removeMediaToWatchlistUseCase: RemoveMediaFromWatchlistUseCase =
        mockk(relaxed = true)
    private val carouselModelToMediaFilterMapper = CarouselModelToMediaFilterMapper()
    private val dateModelMapper = DateModelMapper()
    private val languageModelMapper = LanguageModelMapper()
    private val currencyModelMapper = CurrencyModelMapper()
    private val durationModelMapper = DurationModelMapper(
        stringProvider = stringProvider
    )
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
                assertThat(uiState).isEqualTo(
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
            viewModel.uiEvent.test {
                val event = HomeContract.UserEvent.MediaSelected(MediaModel.mock)
                viewModel.setUserEvent(event)

                val uiEvent = awaitItem()
                assertThat(uiEvent).isEqualTo(
                    HomeContract.UIEvent.NavigateToMedia(
                        mediaId = MediaId(1L),
                        mediaType = MediaType.Movie,
                    )
                )
            }
        }
    }

    @Test
    fun `When watchButton is selected, Then add to watchlist`() {
        coEvery { addMediaToWatchlistUseCase.invoke(any()) } returns ResultData.Success(Media.Movie.mock)

        runTest {
            viewModel.setUserEvent(HomeContract.UserEvent.WatchButtonClicked(MediaModel.mock))

            advanceUntilIdle()
            coVerify { addMediaToWatchlistUseCase.invoke(any()) }
        }
    }

    @Test
    fun `When watchButton is unselected, Then remote from watchlist`() {
        coEvery { removeMediaToWatchlistUseCase(any()) } returns ResultData.Success(true)

        runTest {
            viewModel.setUserEvent(
                HomeContract.UserEvent.WatchButtonClicked(
                    MediaModel.mock.copy(
                        watchlist = true
                    )
                )
            )

            advanceUntilIdle()
            coVerify { removeMediaToWatchlistUseCase(any()) }
        }
    }

    @Test
    fun `When seeAll is selected, Then uiState is updated`() {
        runTest {
            viewModel.uiEvent.test {
                val event = HomeContract.UserEvent.SeeAllButtonClicked(
                    HomeModuleModel.Carousel.Popular.empty
                )
                viewModel.setUserEvent(event)

                val uiEvent = awaitItem()
                assertThat(uiEvent).isEqualTo(
                    HomeContract.UIEvent.NavigateToMedias(
                        mediaType = MediaType.Movie,
                        mediaCategory = MediaCategory.Popular,
                    )
                )
            }
        }
    }

    @Test
    fun `When filter is selected, Then uiState is updated`() {
        coEvery { changeHomeModuleFilterUseCase.invoke(any()) } returns ResultData.Success(true)

        runTest {
            val event = HomeContract.UserEvent.MediaFilterClicked(
                module = HomeModuleModel.Carousel.Popular.empty,
                filter = MediaFilterModel.mockk
            )
            viewModel.setUserEvent(event)

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
