package com.raxdenstudios.app.navigator

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MediaListActivityResultContract

internal class HomeNavigatorImpl(
  private val activity: FragmentActivity
) : HomeNavigator {

  companion object {
    private const val LOGIN_KEY = "login"
    private const val MOVIES_KEY = "movies"
  }

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Unit>
  private lateinit var moviesActivityResultLauncher: ActivityResultLauncher<MediaFilterModel>

  private var onLoginSuccess: () -> Unit = {}
  private var onMoviesRefresh: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    loginActivityResultLauncher = registry.registerLoginActivityResultRegistry(owner)
    moviesActivityResultLauncher = registry.registerMoviesActivityResultRegistry(owner)
  }

  override fun login(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }

  override fun movies(mediaFilterModel: MediaFilterModel, onRefresh: () -> Unit) {
    onMoviesRefresh = onRefresh
    moviesActivityResultLauncher.launch(mediaFilterModel)
  }

  private fun ActivityResultRegistry.registerLoginActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<Unit> =
    register(
      LOGIN_KEY,
      owner,
      LoginActivityResultContract()
    ) { logged -> if (logged) onLoginSuccess() }

  private fun ActivityResultRegistry.registerMoviesActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<MediaFilterModel> =
    register(
      MOVIES_KEY,
      owner,
      MediaListActivityResultContract()
    ) { refresh -> if (refresh) onMoviesRefresh() }
}
