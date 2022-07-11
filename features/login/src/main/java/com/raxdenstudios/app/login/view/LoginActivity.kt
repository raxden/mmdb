package com.raxdenstudios.app.login.view

import android.content.Context
import android.os.Bundle
import androidx.activity.viewModels
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.login.databinding.LoginActivityBinding
import com.raxdenstudios.app.login.view.viewmodel.LoginUIState
import com.raxdenstudios.app.login.view.viewmodel.LoginViewModel
import com.raxdenstudios.app.tmdb.TMDBConnect
import com.raxdenstudios.commons.ext.intentFor
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.setResultOKAndFinish
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.setVirtualNavigationBarSafeArea
import com.raxdenstudios.commons.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context) = context.intentFor<LoginActivity>()
  }

  @Inject
  lateinit var tmdbConnect: TMDBConnect

  @Inject
  lateinit var errorManager: ErrorManager

  private val binding: LoginActivityBinding by viewBinding()
  private val viewModel: LoginViewModel by viewModels()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> handleState(state) }
  }

  private fun handleState(state: LoginUIState): Unit = when (state) {
    is LoginUIState.Error -> errorManager.handleError(state.throwable)
    LoginUIState.Logged -> setResultOKAndFinish()
  }

  private fun LoginActivityBinding.setUp() {
    sigInWithTmdb.setSafeOnClickListener { sigInWithTMDB() }
    setVirtualNavigationBarSafeArea(root)
  }

  private fun sigInWithTMDB() {
    tmdbConnect.sigIn(
      onSuccess = { credentials: Credentials -> viewModel.sigIn(credentials) },
      onError = { throwable -> errorManager.handleError(throwable) }
    )
  }
}
