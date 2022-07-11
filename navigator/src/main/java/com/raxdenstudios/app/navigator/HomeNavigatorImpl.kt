package com.raxdenstudios.app.navigator

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MediaListActivityResultContract
import javax.inject.Inject

internal class HomeNavigatorImpl @Inject constructor(
  activity: FragmentActivity,
  private val loginActivityResultContract: LoginActivityResultContract,
  private val mediaListActivityResultContract: MediaListActivityResultContract,
) : HomeNavigator {

  companion object {
    private const val LOGIN_KEY = "login"
    private const val MOVIES_KEY = "movies"
  }

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Unit>
  private lateinit var mediaListActivityResultLauncher: ActivityResultLauncher<HomeModuleModel.CarouselMedias>

  private var onLoginSuccess: () -> Unit = {}
  private var onMoviesRefresh: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    loginActivityResultLauncher = registry.registerLoginActivityResultRegistry(owner)
    mediaListActivityResultLauncher = registry.registerMediaListActivityResultRegistry(owner)
  }

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

  private fun ActivityResultRegistry.registerLoginActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<Unit> =
    register(
      LOGIN_KEY,
      owner,
      loginActivityResultContract
    ) { logged -> if (logged) onLoginSuccess() }

  private fun ActivityResultRegistry.registerMediaListActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<HomeModuleModel.CarouselMedias> =
    register(
      MOVIES_KEY,
      owner,
      mediaListActivityResultContract
    ) { refresh -> if (refresh) onMoviesRefresh() }
}
