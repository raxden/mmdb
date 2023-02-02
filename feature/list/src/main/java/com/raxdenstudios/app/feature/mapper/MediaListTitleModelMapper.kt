package com.raxdenstudios.app.feature.mapper

import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.feature.model.MediaListParams
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

class MediaListTitleModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
) {

    fun transform(
        params: MediaListParams,
    ) = when (params.mediaFilter.mediaCategory) {
        MediaCategory.NowPlaying -> stringProvider.getString(R.string.list_now_playing_in_theaters)
        MediaCategory.Popular -> when (params.mediaFilter.mediaType) {
            MediaType.Movie -> stringProvider.getString(R.string.list_popular_movies)
            MediaType.TvShow -> stringProvider.getString(R.string.list_popular_tv_shows)
        }
        MediaCategory.TopRated -> when (params.mediaFilter.mediaType) {
            MediaType.Movie -> stringProvider.getString(R.string.list_top_rated_movies)
            MediaType.TvShow -> stringProvider.getString(R.string.list_top_rated_tv_shows)
        }
        MediaCategory.Upcoming -> stringProvider.getString(R.string.list_upcoming_in_theaters)
        MediaCategory.Watchlist -> stringProvider.getString(R.string.list_from_your_watchlist)
    }
}
