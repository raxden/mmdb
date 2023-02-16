package com.raxdenstudios.app.core.network

class APIDataV4Provider : APIDataProvider {

    override val baseUrl: String
        get() = "https://api.themoviedb.org/4/"
    override val baseImageUrl: String
        get() = "https://image.tmdb.org/t/p/"
    override val token: String
        get() = BuildConfig.TMDB_TOKEN_V4
}
