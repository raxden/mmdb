package com.raxdenstudios.app.home

import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.app.home.view.model.HomeModuleModel

interface HomeNavigator : DefaultLifecycleObserver {

  fun launchLogin(onSuccess: () -> Unit = {})
  fun launchMediaList(carouselMedias: HomeModuleModel.CarouselMedias, onRefresh: () -> Unit = {})
}
