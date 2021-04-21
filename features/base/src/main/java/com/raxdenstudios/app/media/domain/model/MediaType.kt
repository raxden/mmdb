package com.raxdenstudios.app.media.domain.model

sealed class MediaType {
  object Movie : MediaType()
  object TVShow : MediaType()
}
