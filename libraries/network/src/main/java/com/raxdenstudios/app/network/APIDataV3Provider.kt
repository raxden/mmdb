package com.raxdenstudios.app.network

import javax.inject.Inject

class APIDataV3Provider @Inject constructor() : APIDataProvider {

    override fun getDomain(): String = "https://api.themoviedb.org/3/"

    override fun getImageDomain(): String = "https://image.tmdb.org/t/p/"

    override fun getToken(): String = "7e92c96cbdbc11b3f52c6fefe4950b96"
}
