package com.raxdenstudios.app.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.app.home.view.util.HomeNavigationDelegate
import com.raxdenstudios.commons.ext.viewBinding
import org.koin.android.ext.android.inject
import org.koin.android.scope.AndroidScopeComponent
import org.koin.androidx.scope.activityScope
import org.koin.core.parameter.parametersOf
import org.koin.core.scope.Scope

class HomeActivity : BaseActivity(), AndroidScopeComponent {

  companion object {
    fun createIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }

  override val scope: Scope by activityScope()

  private val binding: HomeActivityBinding by viewBinding()
  private val homeNavigationDelegate: HomeNavigationDelegate by lazy {
    HomeNavigationDelegate(this, binding)
  }
  private val navigator: HomeNavigator by inject { parametersOf(this) }

  override fun onSaveInstanceState(outState: Bundle) {
    homeNavigationDelegate.onSaveInstanceState(outState)
    super.onSaveInstanceState(outState)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    homeNavigationDelegate.onCreate(savedInstanceState)
    binding.setUp()

    lifecycle.addObserver(navigator)
  }

  private fun HomeActivityBinding.setUp() {

  }
}
