package com.raxdenstudios.app.feature

import androidx.lifecycle.SavedStateHandle
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.feature.model.MediaListParams
import com.raxdenstudios.commons.android.ext.getOrThrow
import javax.inject.Inject

class MediaListParamsFactory @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) {

    fun create(): MediaListParams {
        if (savedStateHandle.contains(MEDIA_ID)) {
            return MediaListParams.Related(
                mediaId = MediaId(
                    savedStateHandle.getOrThrow(MEDIA_ID, "Invalid arguments")
                ),
                mediaType = MediaType.fromValue(
                    savedStateHandle.getOrThrow(MEDIA_TYPE_PARAM, "Invalid arguments")
                ),
            )
        } else {
            return MediaListParams.List(
                mediaFilter = MediaFilter(
                    mediaType = MediaType.fromValue(
                        savedStateHandle.getOrThrow(MEDIA_TYPE_PARAM, "Invalid arguments")
                    ),
                    mediaCategory = MediaCategory.fromValue(
                        savedStateHandle.getOrThrow(MEDIA_CATEGORY_PARAM, "Invalid arguments")
                    ),
                )
            )
        }
    }

    companion object {

        const val MEDIA_ID = "mediaId"
        const val MEDIA_TYPE_PARAM = "mediaType"
        const val MEDIA_CATEGORY_PARAM = "mediaCategory"
    }
}
