package com.raxdenstudios.app.home

import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.app.home.view.model.HomeModuleModel

interface HomeNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit)
  fun movies(homeModuleModel: HomeModuleModel)
}
