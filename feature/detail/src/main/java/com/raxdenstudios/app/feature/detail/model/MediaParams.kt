package com.raxdenstudios.app.feature.detail.model

import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType

data class MediaParams(
    val mediaId: MediaId,
    val mediaType: MediaType,
)
