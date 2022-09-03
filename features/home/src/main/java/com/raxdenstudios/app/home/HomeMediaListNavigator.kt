package com.raxdenstudios.app.home

import com.raxdenstudios.app.home.view.model.HomeModuleModel

interface HomeMediaListNavigator {

    fun launchLogin(onSuccess: () -> Unit = {})
    fun launchMediaList(carouselMedias: HomeModuleModel.CarouselMedias, onRefresh: () -> Unit = {})
}
