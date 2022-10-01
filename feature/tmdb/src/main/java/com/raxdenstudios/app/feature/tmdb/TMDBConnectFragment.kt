package com.raxdenstudios.app.feature.tmdb

import androidx.fragment.app.DialogFragment

class TMDBConnectFragment : DialogFragment() {

    companion object {

        fun newInstance() = TMDBConnectFragment()
    }

//    private val binding: TmdbConnectFragmentBinding by viewBinding()
//    private val viewModel: TMDBViewModel by viewModels()
//
//    private lateinit var webViewWrapper: TMDBWebViewWrapper
//
//    var onSuccess: (credentials: Credentials) -> Unit = {}
//    var onError: (throwable: Throwable) -> Unit = {}
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?,
//    ): View = inflater.inflate(R.layout.tmdb_connect_fragment, container)
//
//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
//        super.onCreateDialog(savedInstanceState).also { dialog -> dialog.setTransparentBackground() }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        binding.setUp()
//
//        observe(viewModel.uiSTate) { state -> binding.handleState(state) }
//    }
//
//    override fun onStart() {
//        super.onStart()
//
//        dialog?.window?.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.MATCH_PARENT
//        )
//    }
//
//    private fun TmdbConnectFragmentBinding.handleState(state: TMDBViewModel.UIState) = when (state) {
//        is TMDBViewModel.UIState.Connected -> handleAccessTokenLoadedState(state)
//        is TMDBViewModel.UIState.Error -> handleErrorState(state)
//        TMDBViewModel.UIState.Loading -> handleLoadingState()
//        is TMDBViewModel.UIState.TokenLoaded -> handleTokenLoadedState(state.token)
//    }
//
//    private fun TmdbConnectFragmentBinding.handleErrorState(state: TMDBViewModel.UIState.Error) {
//        contentLoadingProgressBar.hide()
//        onError(state.throwable)
//        close()
//    }
//
//    private fun TmdbConnectFragmentBinding.handleLoadingState() {
//        contentLoadingProgressBar.show()
//    }
//
//    private fun TmdbConnectFragmentBinding.handleAccessTokenLoadedState(state: TMDBViewModel.UIState.Connected) {
//        contentLoadingProgressBar.hide()
//        onSuccess(state.credentials)
//        close()
//    }
//
//    private fun TmdbConnectFragmentBinding.handleTokenLoadedState(token: String) {
//        webViewWrapper.requestAccess(token) { viewModel.login(token) }
//        contentLoadingProgressBar.hide()
//    }
//
//    private fun TmdbConnectFragmentBinding.setUp() {
//        webViewWrapper = TMDBWebViewWrapper(webView)
//    }
}
