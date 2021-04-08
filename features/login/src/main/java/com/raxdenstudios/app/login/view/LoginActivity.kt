package com.raxdenstudios.app.login.view

import android.content.Context
import android.os.Bundle
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.login.databinding.LoginActivityBinding
import com.raxdenstudios.app.login.view.viewmodel.LoginUIState
import com.raxdenstudios.app.login.view.viewmodel.LoginViewModel
import com.raxdenstudios.app.tmdb.TMDBConnect
import com.raxdenstudios.commons.ext.intentFor
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.setVirtualNavigationBarSafeArea
import com.raxdenstudios.commons.ext.viewBinding
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class LoginActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context) = context.intentFor<LoginActivity>()
  }

  private val binding: LoginActivityBinding by viewBinding()
  private val viewModel: LoginViewModel by viewModel()
  private val tmdbConnect: TMDBConnect by inject { parametersOf(this) }
  private val errorManager: ErrorManager by inject { parametersOf(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> handleState(state) }
  }

  private fun handleState(state: LoginUIState): Unit = when (state) {
    is LoginUIState.Error -> errorManager.handleError(state.throwable)
    LoginUIState.Logged -> finish()
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
