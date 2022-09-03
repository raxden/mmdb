package com.raxdenstudios.app.navigator

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.home.HomeMediaListNavigator
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MediaListActivityResultContract
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class HomeMediaListNavigatorImpl @Inject constructor(
    activity: FragmentActivity,
    loginActivityResultContract: LoginActivityResultContract,
    mediaListActivityResultContract: MediaListActivityResultContract,
) : HomeMediaListNavigator {

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
        onRefresh: () -> Unit,
    ) {
        onMoviesRefresh = onRefresh
        mediaListActivityResultLauncher.launch(carouselMedias)
    }
}
