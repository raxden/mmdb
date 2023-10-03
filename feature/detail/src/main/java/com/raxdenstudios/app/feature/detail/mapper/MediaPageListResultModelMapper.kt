package com.raxdenstudios.app.feature.detail.mapper

import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.android.provider.StringProvider
import javax.inject.Inject

class MediaPageListResultModelMapper @Inject constructor(
    private val mediaModelMapper: MediaModelMapper,
    private val errorModelMapper: ErrorModelMapper,
    private val stringProvider: StringProvider,
) {

    fun transform(result: ResultData<PageList<Media>, ErrorDomain>): ResultData<RelatedMediasModel, ErrorModel> =
        result
            .map { pageList -> pageList.items }
            .map { medias -> medias.map { media -> mediaModelMapper.transform(media) } }
            .map { medias ->
                RelatedMediasModel(
                    label = when (medias.first().mediaType) {
                        MediaType.Movie -> stringProvider.getString(R.string.similar_movies)
                        MediaType.TvShow -> stringProvider.getString(R.string.similar_tv_shows)
                    },
                    medias = medias
                )
            }
            .mapFailure { error -> errorModelMapper.transform(error) }
}
