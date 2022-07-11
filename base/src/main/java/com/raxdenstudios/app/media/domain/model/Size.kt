package com.raxdenstudios.app.media.domain.model

sealed class Size(
  val url: String
) {

  data class Thumbnail(val domain: String, val source: String) :
    Size("${domain}w500$source")

  data class Original(val domain: String, val source: String) :
    Size("${domain}original$source")
}
