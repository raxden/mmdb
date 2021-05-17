package com.raxdenstudios.app.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.home.databinding.HomeV2ActivityBinding
import com.raxdenstudios.app.home.view.util.HomeNavigationDelegate
import com.raxdenstudios.commons.ext.viewBinding

class HomeV2Activity : BaseActivity() {

  companion object {
    fun createIntent(context: Context) = Intent(context, HomeV2Activity::class.java)
  }

  private val binding: HomeV2ActivityBinding by viewBinding()
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
  }

  private fun HomeV2ActivityBinding.setUp() {

  }
}
