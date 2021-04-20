package com.raxdenstudios.app.movie.domain.model

sealed class MediaType {
  object Movie : MediaType()
  object TVShow : MediaType()
}
