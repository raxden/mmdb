package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.mapFailure
import com.raxdenstudios.commons.pagination.model.PageList
import javax.inject.Inject

class PageListMediaModelMapper @Inject constructor(
    private val mediaModelMapper: MediaModelMapper,
    private val errorModelMapper: ErrorModelMapper,
) {

    fun transform(source: ResultData<PageList<Media>, ErrorDomain>): ResultData<PageList<MediaModel>, ErrorModel> =
        source
            .map { pageList -> pageList.map { mediaModelMapper.transform(it) } }
            .mapFailure { error -> errorModelMapper.transform(error) }
}
