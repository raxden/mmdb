package com.raxdenstudios.app.feature.home

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.ChangeHomeModuleFilterUseCase
import com.raxdenstudios.app.core.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.feature.home.mapper.CarouselModelMapper
import com.raxdenstudios.app.feature.home.mapper.CarouselModelToMediaFilterMapper
import com.raxdenstudios.app.feature.home.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import net.lachlanmckee.timberjunit.TimberTestRule
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
        every { this@mockk.invoke() } returns flow { emit(modulesWithMedias) }
    }
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(Media.Movie.empty)
    }
    private val removeMediaToWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(true)
    }
    private val carouselModelToMediaFilterMapper = CarouselModelToMediaFilterMapper()
    private val mediaListItemModelMapper = MediaListItemModelMapper()
    private val carouselModelMapper = CarouselModelMapper(
        stringProvider = stringProvider,
        mediaListItemModelMapper = mediaListItemModelMapper,
    )
    private val homeModuleModelMapper = HomeModuleModelMapper(
        carouselModelMapper = carouselModelMapper
    )
    private val changeHomeModuleFilterUseCase: ChangeHomeModuleFilterUseCase = mockk {
        coEvery { this@mockk.invoke(any()) } returns ResultData.Success(true)
    }

    private val viewModel: HomeViewModel by lazy {
        HomeViewModel(
            getHomeModulesUseCase = getHomeModulesUseCase,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaToWatchlistUseCase,
            homeModuleModelMapper = homeModuleModelMapper,
            changeHomeModuleFilterUseCase = changeHomeModuleFilterUseCase,
            carouselModelToMediaFilterMapper = carouselModelToMediaFilterMapper,
        )
    }

    @Test
    fun `Given a viewModel, When viewModel is started, Then modules with movies are loaded`() =
        runTest {
            viewModel.uiState.test {
                val uiState = awaitItem()
                assertThat(uiState).isEqualTo(HomeContract.UIState.loading())

                val uiState2 = awaitItem()
                assertThat(uiState2).isEqualTo(
                    HomeContract.UIState(
                        modules = listOf(
                            HomeModuleModel.Carousel.Popular.empty.copy(
                                medias = listOf(
                                    MediaModel.empty.copy(id = MediaId(1L)),
                                    MediaModel.empty.copy(id = MediaId(2L)),
                                ),
                                filters = listOf(
                                    MediaFilterModel(
                                        id = MediaType.Movie,
                                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                                        isSelected = true,
                                    ),
                                    MediaFilterModel(
                                        id = MediaType.TvShow,
                                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                                        isSelected = false,
                                    ),
                                )
                            ),
                        ),
                    )
                )
            }
        }

    companion object {

        private val modules = listOf(
            HomeModule.Popular.empty,
        )
        private val medias = listOf(
            Media.Movie.empty.copy(id = MediaId(1)),
            Media.Movie.empty.copy(id = MediaId(2)),
        )
        private val modulesWithMedias: Map<HomeModule, List<Media>> =
            modules.associateWith { medias }
    }
}
