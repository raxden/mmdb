package com.raxdenstudios.app.core.model

import androidx.annotation.VisibleForTesting

sealed class Picture {

    object Empty : Picture()

    data class WithImage(
        val thumbnail: Size.Thumbnail,
        val original: Size.Original,
    ) : Picture() {

        companion object {

            @VisibleForTesting
            val mock = WithImage(
                thumbnail = Size.Thumbnail(
                    url = "https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
                ),
                original = Size.Original(
                    url = "https://image.tmdb.org/t/p/original/6KErczPBROQty7QoIsaa6wJYXZi.jpg",
                ),
            )
        }
    }
}
