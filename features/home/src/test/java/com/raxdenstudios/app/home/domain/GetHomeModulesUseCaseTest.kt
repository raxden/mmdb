package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.test.BaseTest
import io.mockk.mockk
import org.koin.core.module.Module

internal class GetHomeModulesUseCaseTest : BaseTest() {

  private val accountRepository: AccountRepository = mockk()
  private val homeModuleRepository: HomeModuleRepository = mockk()

  override val modules: List<Module>
    get() = emptyList()

  private val useCase: GetHomeModulesUseCase by lazy {
    GetHomeModulesUseCase(
      accountRepository,
      homeModuleRepository
    )
  }
}