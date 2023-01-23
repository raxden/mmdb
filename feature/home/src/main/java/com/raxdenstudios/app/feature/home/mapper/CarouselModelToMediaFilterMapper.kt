package com.raxdenstudios.app.feature.home.mapper

import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class CarouselModelToMediaFilterMapper @Inject constructor() :
    DataMapper<HomeModuleModel.Carousel, MediaFilter>() {

    override fun transform(source: HomeModuleModel.Carousel): MediaFilter {
        val mediaType = source.filters.find { it.isSelected }?.id ?: MediaType.Movie
        val mediaCategory = when (source) {
            is HomeModuleModel.Carousel.NowPlaying -> MediaCategory.NowPlaying
            is HomeModuleModel.Carousel.Popular -> MediaCategory.Popular
            is HomeModuleModel.Carousel.TopRated -> MediaCategory.TopRated
            is HomeModuleModel.Carousel.Upcoming -> MediaCategory.Upcoming
            is HomeModuleModel.Carousel.Watchlist -> MediaCategory.Watchlist
        }
        return MediaFilter(
            mediaType = mediaType,
            mediaCategory = mediaCategory
        )
    }
}
