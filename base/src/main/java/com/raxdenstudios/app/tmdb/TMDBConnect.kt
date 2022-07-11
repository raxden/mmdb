package com.raxdenstudios.app.tmdb

import com.raxdenstudios.app.account.domain.model.Credentials

interface TMDBConnect {
  fun sigIn(
    onSuccess: (credentials: Credentials) -> Unit,
    onError: (throwable: Throwable) -> Unit
  )
}
