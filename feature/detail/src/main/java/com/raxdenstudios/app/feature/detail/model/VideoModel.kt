package com.raxdenstudios.app.feature.detail.model

import org.jetbrains.annotations.VisibleForTesting

data class VideoModel(
    val name: String,
    val uri: String,
    val thumbnail: String = "",
) {

    companion object {

        @VisibleForTesting
        val mock = VideoModel(
            name = "The Last of Us",
            uri = "https://www.youtube.com/watch?v=l6rAoph5UgI",
            thumbnail = "",
        )
    }
}
