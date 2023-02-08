package com.raxdenstudios.app.core.model

sealed interface Size {

    val url: String

    data class Thumbnail(
        override val url: String
    ) : Size

    data class Original(
        override val url: String
    ) : Size
}
