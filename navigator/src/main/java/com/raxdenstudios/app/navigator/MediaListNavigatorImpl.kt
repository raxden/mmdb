package com.raxdenstudios.app.navigator

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
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
