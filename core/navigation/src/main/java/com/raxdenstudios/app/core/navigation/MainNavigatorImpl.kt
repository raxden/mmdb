package com.raxdenstudios.app.core.navigation

//@ActivityScoped
//class MainNavigatorImpl @Inject constructor(
//    private val activity: ComponentActivity,
//    loginActivityResultContract: LoginActivityResultContract,
//) : MainNavigator {
//
//    private val loginActivityResultLauncher =
//        activity.registerForActivityResult(loginActivityResultContract) { logged ->
//            if (logged) onLoginSuccess()
//        }
//
//    private var onLoginSuccess: () -> Unit = {}
//
//    override fun launchLogin(onSuccess: () -> Unit) {
//        onLoginSuccess = onSuccess
//        loginActivityResultLauncher.launch(Unit)
//    }
//
//    override fun navigateToMediaList(
//        mediaFilter: MediaFilter,
//    ) {
//        val params = MediaListParams(
//            mediaFilter = mediaFilter
//        )
//        val intent = MediaListActivity.createIntent(activity, params)
//        activity.startActivity(intent)
//    }
//
//    override fun navigateTo(route: String) {
//        TODO("Not yet implemented")
//    }
//}
