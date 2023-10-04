package com.raxdenstudios.app.feature.detail.mapper

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import javax.inject.Inject

class MediaResultModelMapper @Inject constructor(
    private val mediaModelMapper: MediaModelMapper,
    private val errorModelMapper: ErrorModelMapper,
) {

    fun transform(result: ResultData<Media, ErrorDomain>): ResultData<MediaModel, ErrorModel> =
        result.map { media -> mediaModelMapper.transform(media) }
            .mapFailure { error -> errorModelMapper.transform(error) }
}
