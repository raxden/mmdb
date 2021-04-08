package com.raxdenstudios.app.login.di

import com.raxdenstudios.app.login.domain.LoginUseCase
import com.raxdenstudios.app.login.view.viewmodel.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val loginFeatureModule = module {

  factory { LoginUseCase(get()) }

  viewModel { LoginViewModel(get()) }
}
