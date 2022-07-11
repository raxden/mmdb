package com.raxdenstudios.app.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.app.home.view.util.HomeNavigationDelegate
import com.raxdenstudios.commons.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context) = Intent(context, HomeActivity::class.java)
  }

  @Inject
  lateinit var navigator: HomeNavigator

  private val binding: HomeActivityBinding by viewBinding()
  private val homeNavigationDelegate: HomeNavigationDelegate by lazy {
    HomeNavigationDelegate(this, binding)
  }

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
