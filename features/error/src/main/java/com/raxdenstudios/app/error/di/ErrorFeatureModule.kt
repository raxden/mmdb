package com.raxdenstudios.app.error.di

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.error.ErrorManagerImpl
import org.koin.dsl.module

val errorFeatureModule = module {

  factory<ErrorManager> { (activity: FragmentActivity) -> ErrorManagerImpl(activity) }
}
