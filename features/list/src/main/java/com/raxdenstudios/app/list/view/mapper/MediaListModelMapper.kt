package com.raxdenstudios.app.list.view.mapper

import com.raxdenstudios.app.list.R
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

internal class MediaListModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
    private val mediaListItemModelMapper: MediaListItemModelMapper,
) {

    fun transform(
        params: MediaListParams,
        medias: List<Media>,
    ) = MediaListModel(
        title = when (params) {
            MediaListParams.NowPlaying -> stringProvider.getString(R.string.list_now_playing_movies)
            is MediaListParams.Popular -> when (params.mediaType) {
                MediaType.MOVIE -> stringProvider.getString(R.string.list_popular_movies)
                MediaType.TV_SHOW -> stringProvider.getString(R.string.list_popular_tv_shows)
            }
            is MediaListParams.TopRated -> when (params.mediaType) {
                MediaType.MOVIE -> stringProvider.getString(R.string.list_top_rated_movies)
                MediaType.TV_SHOW -> stringProvider.getString(R.string.list_top_rated_tv_shows)
            }
            MediaListParams.Upcoming -> stringProvider.getString(R.string.list_upcoming_movies)
            is MediaListParams.WatchList -> stringProvider.getString(R.string.list_watch_list)
        },
        items = mediaListItemModelMapper.transform(medias),
    )
}
