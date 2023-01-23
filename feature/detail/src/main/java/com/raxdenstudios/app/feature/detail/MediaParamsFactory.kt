package com.raxdenstudios.app.feature.detail

import androidx.lifecycle.SavedStateHandle
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.feature.detail.model.MediaParams
import com.raxdenstudios.commons.ext.getOrThrow
import javax.inject.Inject

class MediaParamsFactory @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) {

    fun create(): MediaParams = MediaParams(
        mediaId = MediaId(
            savedStateHandle.getOrThrow(MEDIA_ID_PARAM, "Invalid arguments")
        ),
        mediaType = MediaType.fromValue(
            savedStateHandle.getOrThrow(MEDIA_TYPE_PARAM, "Invalid arguments")
        ),
    )

    companion object {

        const val MEDIA_TYPE_PARAM = "mediaType"
        const val MEDIA_ID_PARAM = "mediaId"
    }
}
