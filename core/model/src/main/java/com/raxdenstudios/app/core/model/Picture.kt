package com.raxdenstudios.app.core.model

sealed class Picture {

    object Empty : Picture()

    data class WithImage(
        val thumbnail: Size.Thumbnail,
        val original: Size.Original,
    ) : Picture() {

        companion object {

            val empty = WithImage(
                thumbnail = Size.Thumbnail(""),
                original = Size.Original(""),
            )
        }
    }
}
