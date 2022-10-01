package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.coFlatMap
import com.raxdenstudios.commons.ext.map
import javax.inject.Inject

class ChangeHomeModuleFilterUseCase @Inject constructor(
    private val homeModuleRepository: HomeModuleRepository,
) {

    suspend operator fun invoke(params: Params): ResultData<Boolean> =
        homeModuleRepository.get(params.moduleId)
            .map { module -> module.copyValue(mediaType = params.mediaType) }
            .coFlatMap { module -> homeModuleRepository.save(module) }

    data class Params(
        val moduleId: Long,
        val mediaType: MediaType,
    )
}