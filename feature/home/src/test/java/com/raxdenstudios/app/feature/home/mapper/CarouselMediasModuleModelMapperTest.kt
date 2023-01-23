package com.raxdenstudios.app.feature.home.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.feature.home.R
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.mockk
import org.junit.Test

class CarouselMediasModuleModelMapperTest {

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val durationModelMapper = DurationModelMapper(
        stringProvider = stringProvider
    )
    private val mediaModelMapper = MediaModelMapper(
        durationModelMapper = durationModelMapper,
    )

    private val mapper: CarouselModelMapper by lazy {
        CarouselModelMapper(
            stringProvider = stringProvider,
            mediaModelMapper = mediaModelMapper,
        )
    }

    @Test
    fun `Given a NowPlaying module and resultData with movies, When transform is called, Then return a NowPlaying carousel`() {
        val module = HomeModule.NowPlaying.empty

        val result = mapper.transform(module, medias)

        assertThat(result).isEqualTo(
            HomeModuleModel.Carousel.NowPlaying.empty.copy(
                medias = listOf(MediaModel.empty.copy(id = MediaId(1L)))
            )
        )
    }

    @Test
    fun `Given a Popular module and resultData with movies, When transform is called, Then return a Popular carousel`() {
        val module = HomeModule.Popular.empty

        val result = mapper.transform(module, medias)

        assertThat(result).isEqualTo(
            HomeModuleModel.Carousel.Popular.empty.copy(
                medias = listOf(MediaModel.empty.copy(id = MediaId(1L))),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = module.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = module.mediaType == MediaType.TvShow,
                    ),
                )
            )
        )
    }

    @Test
    fun `Given a TopRated module and resultData with movies, When transform is called, Then return a TopRated carousel`() {
        val module = HomeModule.TopRated.empty

        val result = mapper.transform(module, medias)

        assertThat(result).isEqualTo(
            HomeModuleModel.Carousel.TopRated.empty.copy(
                medias = listOf(MediaModel.empty.copy(id = MediaId(1L))),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = module.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = module.mediaType == MediaType.TvShow,
                    ),
                )
            )
        )
    }

    @Test
    fun `Given a Upcoming module and resultData with movies, When transform is called, Then return a Upcoming carousel`() {
        val module = HomeModule.Upcoming.empty

        val result = mapper.transform(module, medias)

        assertThat(result).isEqualTo(
            HomeModuleModel.Carousel.Upcoming.empty.copy(
                medias = listOf(MediaModel.empty.copy(id = MediaId(1L)))
            )
        )
    }

    @Test
    fun `Given a Watchlist module and resultData with movies, When transform is called, Then return a Watchlist carousel`() {
        val module = HomeModule.Watchlist.empty

        val result = mapper.transform(module, medias)

        assertThat(result).isEqualTo(
            HomeModuleModel.Carousel.Watchlist.empty.copy(
                medias = listOf(MediaModel.empty.copy(id = MediaId(1L))),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = module.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = module.mediaType == MediaType.TvShow,
                    ),
                )
            )
        )
    }

    companion object {

        private val medias = listOf(
            Media.Movie.empty.copy(id = MediaId(1L))
        )
    }
}
