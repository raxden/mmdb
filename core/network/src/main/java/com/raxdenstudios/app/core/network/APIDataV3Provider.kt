package com.raxdenstudios.app.core.network

import javax.inject.Inject

class APIDataV3Provider @Inject constructor() : APIDataProvider {

    override fun getDomain(): String = "https://api.themoviedb.org/3/"

    override fun getImageDomain(): String = "https://image.tmdb.org/t/p/"

    override fun getToken(): String = BuildConfig.TMDB_TOKEN_V3
}
