package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Test

internal class CarouselMediasModelMapperTest : BaseTest() {

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val mediaListItemModelMapper = MediaListItemModelMapper()

    private val mapper: CarouselMediasModelMapper by lazy {
        CarouselMediasModelMapper(
            stringProvider = stringProvider,
            mediaListItemModelMapper = mediaListItemModelMapper,
        )
    }

    @Test
    fun `Given a NowPlaying module and resultData with movies, When transform is called, Then return a NowPlaying carousel`() {
        val module = HomeModule.nowPlayingMovies.copy(medias = aMedias)

        val result = mapper.transform(module)

        assertEquals(
            HomeModuleModel.CarouselMedias.NowPlaying.empty.copy(
                medias = listOf(MediaListItemModel.empty.copy(id = MediaId(1L)))
            ),
            result
        )
    }

    @Test
    fun `Given a Popular module and resultData with movies, When transform is called, Then return a Popular carousel`() {
        val module = HomeModule.popularMovies.copy(medias = aMedias)

        val result = mapper.transform(module)

        assertEquals(
            HomeModuleModel.CarouselMedias.Popular.empty.copy(
                medias = listOf(MediaListItemModel.empty.copy(id = MediaId(1L)))
            ),
            result
        )
    }

    @Test
    fun `Given a TopRated module and resultData with movies, When transform is called, Then return a TopRated carousel`() {
        val module = HomeModule.topRatedMovies.copy(medias = aMedias)

        val result = mapper.transform(module)

        assertEquals(
            HomeModuleModel.CarouselMedias.TopRated.empty.copy(
                medias = listOf(MediaListItemModel.empty.copy(id = MediaId(1L)))
            ),
            result
        )
    }

    @Test
    fun `Given a Upcoming module and resultData with movies, When transform is called, Then return a Upcoming carousel`() {
        val module = HomeModule.upcomingMovies.copy(medias = aMedias)

        val result = mapper.transform(module)

        assertEquals(
            HomeModuleModel.CarouselMedias.Upcoming.empty.copy(
                medias = listOf(MediaListItemModel.empty.copy(id = MediaId(1L)))
            ),
            result
        )
    }

    @Test
    fun `Given a WatchList module and resultData with movies, When transform is called, Then return a WatchList carousel`() {
        val module = HomeModule.watchListMovies.copy(medias = aMedias)

        val result = mapper.transform(module)

        assertEquals(
            HomeModuleModel.CarouselMedias.WatchList.empty.copy(
                medias = listOf(MediaListItemModel.empty.copy(id = MediaId(1L)))
            ),
            result
        )
    }
}

private val aMedias = listOf(
    Media.Movie.empty.copy(id = MediaId(1L))
)
