package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.ConfigProvider
import com.raxdenstudios.app.core.network.model.ContentRatingDto
import com.raxdenstudios.app.core.network.model.ResultsDto
import javax.inject.Inject

class ContentRatingDtoToDomainMapper @Inject constructor(
    private val configProvider: ConfigProvider,
) {

    fun transform(source: ResultsDto<ContentRatingDto>): String {
        return source.results
            .firstOrNull { it.iso_3166_1 == configProvider.region }?.rating ?: ""
    }
}
