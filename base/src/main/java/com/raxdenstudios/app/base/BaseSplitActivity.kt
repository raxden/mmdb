package com.raxdenstudios.app.base

import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat

abstract class BaseSplitActivity : BaseActivity() {

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }
}
