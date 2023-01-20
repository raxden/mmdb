package com.raxdenstudios.app.core.navigation

object HomeRoutes {

    val home = NavigationRoute("home")
    val medias = NavigationRoute("medias/{mediaType}/{mediaCategory}")
    val media = NavigationRoute("media/{mediaId}")
}

object MainRoutes {

    val home = NavigationRoute("mainHome")
    val search = NavigationRoute("mainSearch")
    val account = NavigationRoute("mainAccount")
}
