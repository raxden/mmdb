package com.raxdenstudios.app.home.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.commons.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    companion object {

        fun createIntent(context: Context) = Intent(context, HomeActivity::class.java)
    }

    private val binding: HomeActivityBinding by viewBinding()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.setUp()
    }

    private fun HomeActivityBinding.setUp() {

    }
}
