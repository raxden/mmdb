package com.raxdenstudios.app.core.navigation

//@ActivityScoped
//class MediaListNavigatorImpl @Inject constructor(
//    private val activity: FragmentActivity,
//    loginActivityResultContract: LoginActivityResultContract,
//) : MediaListNavigator {
//
//    private val loginActivityResultLauncher =
//        activity.registerForActivityResult(loginActivityResultContract) { logged ->
//            if (logged) onLoginSuccess()
//        }
//
//    private var onLoginSuccess: () -> Unit = {}
//
//    override fun back() {
//        activity.finish()
//    }
//
//    override fun media(mediaId: MediaId) {
//        // TODO implement
//    }
//
//    override fun login(onSuccess: () -> Unit) {
//        onLoginSuccess = onSuccess
//        loginActivityResultLauncher.launch(Unit)
//    }
//}
