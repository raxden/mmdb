package com.raxdenstudios.app.feature.home.component

import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel

object HomePreviewData {

    private val filters = listOf(
        MediaFilterModel(
            id = MediaType.Movie,
            label = "Movies",
            isSelected = true,
        ),
        MediaFilterModel(
            id = MediaType.TvShow,
            label = "Series",
            isSelected = false,
        ),
    )

    @SuppressWarnings("MagicNumber")
    private val medias = List(10) {
        MediaModel.empty.copy(
            id = MediaId(it.toLong()),
            title = "The Batman",
            releaseDate = "2011",
            rating = "7.8",
            poster = "https://image.tmdb.org/t/p/w500/8Y43POKjjKDGI9MH89NW0NAzzp8.jpg"
        )
    }
    val nowPlayingModule = HomeModuleModel.Carousel.NowPlaying.empty.copy(
        id = 1L,
        label = "Now playing",
        medias = medias,
    )
    val popularModule = HomeModuleModel.Carousel.Popular.empty.copy(
        id = 2L,
        label = "Popular",
        medias = medias,
        filters = filters,
    )
    val topRatedModule = HomeModuleModel.Carousel.TopRated.empty.copy(
        id = 3L,
        label = "TopRated",
        medias = medias,
        filters = filters,
    )
    val upcomingModule = HomeModuleModel.Carousel.Upcoming.empty.copy(
        id = 4L,
        label = "Upcoming",
        medias = medias
    )
    val watchlistModule = HomeModuleModel.Carousel.Watchlist.empty.copy(
        id = 5L,
        label = "Watchlist",
        medias = medias,
        filters = filters,
    )
    val modules = listOf(
        nowPlayingModule,
        popularModule,
        topRatedModule,
        upcomingModule,
        watchlistModule
    )
}
