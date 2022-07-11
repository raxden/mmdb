package com.raxdenstudios.app.tmdb.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseFragmentDialog
import com.raxdenstudios.app.tmdb.R
import com.raxdenstudios.app.tmdb.databinding.TmdbConnectFragmentBinding
import com.raxdenstudios.app.tmdb.view.component.TMDBWebViewWrapper
import com.raxdenstudios.app.tmdb.view.viewmodel.TMDBConnectUIState
import com.raxdenstudios.app.tmdb.view.viewmodel.TMDBViewModel
import com.raxdenstudios.commons.ext.close
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.viewBinding

internal class TMDBConnectFragment : BaseFragmentDialog(R.layout.tmdb_connect_fragment) {

  companion object {
    fun newInstance() = TMDBConnectFragment()
  }

  private val binding: TmdbConnectFragmentBinding by viewBinding()
  private val viewModel: TMDBViewModel by viewModels()

  private lateinit var webViewWrapper: TMDBWebViewWrapper

  var onSuccess: (credentials: Credentials) -> Unit = {}
  var onError: (throwable: Throwable) -> Unit = {}

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }
  }

  private fun TmdbConnectFragmentBinding.handleState(state: TMDBConnectUIState) = when (state) {
    is TMDBConnectUIState.Connected -> handleAccessTokenLoadedState(state)
    is TMDBConnectUIState.Error -> handleErrorState(state)
    TMDBConnectUIState.Loading -> handleLoadingState()
    is TMDBConnectUIState.TokenLoaded -> handleTokenLoadedState(state.token)
  }

  private fun TmdbConnectFragmentBinding.handleErrorState(state: TMDBConnectUIState.Error) {
    contentLoadingProgressBar.hide()
    onError(state.throwable)
    close()
  }

  private fun TmdbConnectFragmentBinding.handleLoadingState() {
    contentLoadingProgressBar.show()
  }

  private fun TmdbConnectFragmentBinding.handleAccessTokenLoadedState(state: TMDBConnectUIState.Connected) {
    contentLoadingProgressBar.hide()
    onSuccess(state.credentials)
    close()
  }

  private fun TmdbConnectFragmentBinding.handleTokenLoadedState(token: String) {
    webViewWrapper.requestAccess(token) { viewModel.login(token) }
    contentLoadingProgressBar.hide()
  }

  private fun TmdbConnectFragmentBinding.setUp() {
    webViewWrapper = TMDBWebViewWrapper(webView)
  }
}
