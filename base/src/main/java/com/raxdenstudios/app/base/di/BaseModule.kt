package com.raxdenstudios.app.base.di

import com.raxdenstudios.commons.ActivityHolder
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.provider.StringProvider
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

val baseModule = module {

  factory { ActivityHolder() }
  factory { StringProvider(get()) }
  factory<DispatcherFacade> {
    object : DispatcherFacade {
      override fun io() = Dispatchers.IO
      override fun default() = Dispatchers.Default
    }
  }
}
