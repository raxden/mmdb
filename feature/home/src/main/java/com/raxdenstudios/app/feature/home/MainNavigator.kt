package com.raxdenstudios.app.feature.home

import com.raxdenstudios.app.core.model.MediaFilter

interface MainNavigator {

    fun launchLogin(onSuccess: () -> Unit = {})
    fun navigateToMediaList(mediaFilter: MediaFilter)
    fun navigateTo(route: String)
}
