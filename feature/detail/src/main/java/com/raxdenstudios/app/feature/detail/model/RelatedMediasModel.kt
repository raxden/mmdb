package com.raxdenstudios.app.feature.detail.model

import com.raxdenstudios.app.core.ui.model.MediaModel
import org.jetbrains.annotations.VisibleForTesting

data class RelatedMediasModel(
    val label: String,
    val medias: List<MediaModel>,
) {

    companion object {

        val empty = RelatedMediasModel(
            label = "",
            medias = emptyList(),
        )

        @VisibleForTesting
        val mock = RelatedMediasModel(
            label = "",
            medias = listOf(
                MediaModel.mock,
            )
        )
    }
}
