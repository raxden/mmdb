package com.raxdenstudios.app.navigator

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import javax.inject.Inject

internal class MediaListNavigatorImpl @Inject constructor(
  activity: FragmentActivity
) : MediaListNavigator {

  companion object {
    private const val LOGIN_KEY = "login"
  }

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Unit>

  private var onLoginSuccess: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    loginActivityResultLauncher = registry.registerLoginActivityResultRegistry(owner)
  }

  override fun login(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }

  private fun ActivityResultRegistry.registerLoginActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<Unit> =
    register(
      LOGIN_KEY,
      owner,
      LoginActivityResultContract()
    ) { logged -> if (logged) onLoginSuccess() }
}
