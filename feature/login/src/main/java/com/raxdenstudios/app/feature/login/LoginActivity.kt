package com.raxdenstudios.app.feature.login

//@AndroidEntryPoint
//class LoginActivity : BaseActivity() {
//
//    companion object {
//
//        fun createIntent(context: Context) = context.intentFor<LoginActivity>()
//    }
//
//    @Inject
//    lateinit var tmdbConnect: TMDBConnect
//
//    @Inject
//    lateinit var errorManager: ErrorManager
//
//    private val binding: LoginActivityBinding by viewBinding()
//    private val viewModel: LoginViewModel by viewModels()
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//
//        binding.setUp()
//
//        observe(viewModel.state) { state -> handleState(state) }
//    }
//
//    private fun handleState(state: LoginViewModel.UIState): Unit = when (state) {
//        is LoginViewModel.UIState.Error -> errorManager.handleError(state.throwable)
//        LoginViewModel.UIState.Logged -> setResultOKAndFinish()
//    }
//
//    private fun LoginActivityBinding.setUp() {
//        sigInWithTmdb.setSafeOnClickListener { sigInWithTMDB() }
//        setVirtualNavigationBarSafeArea(root)
//    }
//
//    private fun sigInWithTMDB() {
//        tmdbConnect.sigIn(
//            onSuccess = { credentials: Credentials -> viewModel.sigIn(credentials) },
//            onError = { throwable -> errorManager.handleError(throwable) }
//        )
//    }
//}
