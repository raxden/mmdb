package com.raxdenstudios.app.navigator.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.raxdenstudios.app.login.view.LoginActivity
import javax.inject.Inject

internal class LoginActivityResultContract @Inject constructor() :
  ActivityResultContract<Unit, Boolean>() {

  override fun createIntent(context: Context, input: Unit): Intent =
    LoginActivity.createIntent(context)

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
    resultCode == Activity.RESULT_OK
}
