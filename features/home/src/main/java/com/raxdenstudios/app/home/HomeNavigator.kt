package com.raxdenstudios.app.home

import com.raxdenstudios.app.home.view.model.HomeModuleModel

interface HomeNavigator {

  fun launchLogin(onSuccess: () -> Unit = {})
  fun launchMediaList(carouselMedias: HomeModuleModel.CarouselMedias, onRefresh: () -> Unit = {})
}
