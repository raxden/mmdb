package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

internal class GetHomeModulesUseCase(
  private val accountRepository: AccountRepository,
  private val homeModuleRepository: HomeModuleRepository,
) {

  fun execute(): Flow<Pair<Account, List<HomeModule>>> =
    combine(
      accountRepository.observeAccount(),
      homeModuleRepository.observeModules()
    ) { account: Account, modules: List<HomeModule> ->
      Pair(account, modules)
    }
}
