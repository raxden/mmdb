package com.raxdenstudios.app.feature.home.mapper

import com.raxdenstudios.app.core.ui.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.feature.home.R
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

class CarouselModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
    private val mediaListItemModelMapper: MediaListItemModelMapper,
) {

    fun transform(
        module: HomeModule,
        medias: List<Media>,
    ): HomeModuleModel.Carousel = when (module) {
        is HomeModule.NowPlaying -> HomeModuleModel.Carousel.NowPlaying(
            id = module.id,
            label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
            medias = mediaListItemModelMapper.transform(medias),
            filters = emptyList(),
        )
        is HomeModule.Popular -> HomeModuleModel.Carousel.Popular(
            id = module.id,
            label = stringProvider.getString(R.string.home_carousel_popular),
            medias = mediaListItemModelMapper.transform(medias),
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
            ),
        )
        is HomeModule.TopRated -> HomeModuleModel.Carousel.TopRated(
            id = module.id,
            label = stringProvider.getString(R.string.home_carousel_top_rated),
            medias = mediaListItemModelMapper.transform(medias),
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
            ),
        )
        is HomeModule.Upcoming -> HomeModuleModel.Carousel.Upcoming(
            id = module.id,
            label = stringProvider.getString(R.string.home_carousel_upcoming),
            medias = mediaListItemModelMapper.transform(medias),
            filters = emptyList(),
        )
        is HomeModule.Watchlist -> HomeModuleModel.Carousel.Watchlist(
            id = module.id,
            label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
            medias = mediaListItemModelMapper.transform(medias),
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
            ),
        )
    }
}
