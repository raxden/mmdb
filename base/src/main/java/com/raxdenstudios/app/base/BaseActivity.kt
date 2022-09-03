package com.raxdenstudios.app.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity : AppCompatActivity() {

    override fun onBackPressed() {
        finishAfterTransition()
    }
}
