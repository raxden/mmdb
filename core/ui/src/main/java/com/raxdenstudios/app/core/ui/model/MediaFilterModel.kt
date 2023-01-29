package com.raxdenstudios.app.core.ui.model

import com.raxdenstudios.app.core.model.MediaType
import org.jetbrains.annotations.VisibleForTesting

data class MediaFilterModel(
    val id: MediaType,
    val label: String,
    val isSelected: Boolean,
) {

    companion object {

        @VisibleForTesting
        val mockk = MediaFilterModel(
            id = MediaType.Movie,
            label = "Movies",
            isSelected = false,
        )
    }
}
