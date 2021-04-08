package com.raxdenstudios.app.base

import android.content.Context
import android.os.Bundle
import com.google.android.play.core.splitcompat.SplitCompat
import org.koin.core.context.loadKoinModules
import org.koin.core.module.Module

abstract class BaseSplitActivity : BaseActivity() {

  private val loadModules by lazy { loadKoinModules(modules) }
  protected abstract val modules: List<Module>

  override fun attachBaseContext(newBase: Context) {
    super.attachBaseContext(newBase)
    SplitCompat.installActivity(this)
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    injectModules()
  }

  private fun injectModules() = loadModules
}

