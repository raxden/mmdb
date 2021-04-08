package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow

internal class GetHomeModulesUseCase(
  private val homeModuleRepository: HomeModuleRepository,
) {

  fun execute(): Flow<List<HomeModule>> =
    homeModuleRepository.modules()
}
