package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.ConfigProvider
import com.raxdenstudios.app.core.network.model.RegionReleaseDateDto
import com.raxdenstudios.app.core.network.model.ResultsDto
import javax.inject.Inject

class CertificationDtoToDomainMapper @Inject constructor(
    private val configProvider: ConfigProvider,
) {

    fun transform(source: ResultsDto<RegionReleaseDateDto>): String {
        return source.results
            .firstOrNull { it.iso_3166_1 == configProvider.region }?.release_dates?.firstOrNull()?.certification ?: ""
    }
}
