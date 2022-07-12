package com.raxdenstudios.app.navigator

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import javax.inject.Inject

internal class MediaListNavigatorImpl @Inject constructor(
  activity: FragmentActivity,
  loginActivityResultContract: LoginActivityResultContract,
) : MediaListNavigator {

  private val loginActivityResultLauncher =
    activity.registerForActivityResult(loginActivityResultContract) { logged ->
      if (logged) onLoginSuccess()
    }

  private var onLoginSuccess: () -> Unit = {}

  override fun login(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }
}
