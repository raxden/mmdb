package com.raxdenstudios.app.base.di

import com.raxdenstudios.commons.ActivityHolder
import com.raxdenstudios.commons.provider.StringProvider
import org.koin.dsl.module

val baseModule = module {

  factory { ActivityHolder() }
  factory { StringProvider(get()) }
}
