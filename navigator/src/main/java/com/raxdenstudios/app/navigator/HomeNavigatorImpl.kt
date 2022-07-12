package com.raxdenstudios.app.navigator

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MediaListActivityResultContract
import javax.inject.Inject

internal class HomeNavigatorImpl @Inject constructor(
  activity: FragmentActivity,
  loginActivityResultContract: LoginActivityResultContract,
  mediaListActivityResultContract: MediaListActivityResultContract,
) : HomeNavigator {

  private val loginActivityResultLauncher =
    activity.registerForActivityResult(loginActivityResultContract) { logged ->
      if (logged) onLoginSuccess()
    }
  private val mediaListActivityResultLauncher =
    activity.registerForActivityResult(mediaListActivityResultContract) { refresh ->
      if (refresh) onMoviesRefresh()
    }

  private var onLoginSuccess: () -> Unit = {}
  private var onMoviesRefresh: () -> Unit = {}

  override fun launchLogin(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }

  override fun launchMediaList(
    carouselMedias: HomeModuleModel.CarouselMedias,
    onRefresh: () -> Unit
  ) {
    onMoviesRefresh = onRefresh
    mediaListActivityResultLauncher.launch(carouselMedias)
  }
}
