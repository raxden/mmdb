package com.raxdenstudios.app.feature.model

import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType

sealed interface MediaListParams {

    data class List(
        val mediaFilter: MediaFilter,
    ) : MediaListParams

    data class Related(
        val mediaId: MediaId,
        val mediaType: MediaType,
    ) : MediaListParams
}
