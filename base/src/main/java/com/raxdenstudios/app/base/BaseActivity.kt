package com.raxdenstudios.app.base

import androidx.appcompat.app.AppCompatActivity
import com.raxdenstudios.commons.ActivityHolder
import org.koin.android.ext.android.inject

abstract class BaseActivity : AppCompatActivity() {

  private val activityHolder: ActivityHolder by inject()

  override fun onStart() {
    super.onStart()
    activityHolder.attach(this)
  }

  override fun onStop() {
    activityHolder.detach(this)
    super.onStop()
  }

  override fun onBackPressed() {
    finishAfterTransition()
  }
}
