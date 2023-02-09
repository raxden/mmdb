package com.raxdenstudios.app.feature.home.mapper

import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

class CarouselModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
    private val mediaModelMapper: MediaModelMapper,
) : DataMapper<HomeModule.Carousel, HomeModuleModel.Carousel>() {

    override fun transform(source: HomeModule.Carousel): HomeModuleModel.Carousel {
        return when (source.mediaCategory) {
            MediaCategory.NowPlaying -> HomeModuleModel.Carousel.NowPlaying(
                id = source.id,
                label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
                medias = mediaModelMapper.transform(source.medias),
                filters = emptyList(),
            )
            MediaCategory.Popular -> HomeModuleModel.Carousel.Popular(
                id = source.id,
                label = stringProvider.getString(R.string.home_carousel_popular),
                medias = mediaModelMapper.transform(source.medias),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = source.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = source.mediaType == MediaType.TvShow,
                    ),
                ),
            )
            MediaCategory.TopRated -> HomeModuleModel.Carousel.TopRated(
                id = source.id,
                label = stringProvider.getString(R.string.home_carousel_top_rated),
                medias = mediaModelMapper.transform(source.medias),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = source.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = source.mediaType == MediaType.TvShow,
                    ),
                ),
            )
            MediaCategory.Upcoming -> HomeModuleModel.Carousel.Upcoming(
                id = source.id,
                label = stringProvider.getString(R.string.home_carousel_upcoming),
                medias = mediaModelMapper.transform(source.medias),
                filters = emptyList(),
            )
            MediaCategory.Watchlist -> HomeModuleModel.Carousel.Watchlist(
                id = source.id,
                label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
                medias = mediaModelMapper.transform(source.medias),
                filters = listOf(
                    MediaFilterModel(
                        id = MediaType.Movie,
                        label = stringProvider.getString(R.string.media_list_item_chip_movies),
                        isSelected = source.mediaType == MediaType.Movie,
                    ),
                    MediaFilterModel(
                        id = MediaType.TvShow,
                        label = stringProvider.getString(R.string.media_list_item_chip_tv_series),
                        isSelected = source.mediaType == MediaType.TvShow,
                    ),
                ),
            )
        }
    }
}
