package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject

class MediaDetailDtoToDomainMapper @Inject constructor(
    private val movieDetailDtoToDomainMapper: MovieDetailDtoToDomainMapper,
    private val tvShowDetailDtoToDomainMapper: TVShowDetailDtoToDomainMapper,
) : DataMapper<MediaDetailDto, Media>() {

    override fun transform(source: MediaDetailDto): Media = source.toDomain()

    private fun MediaDetailDto.toDomain() = when (this) {
        is MediaDetailDto.Movie -> movieDetailDtoToDomainMapper.transform(this)
        is MediaDetailDto.TVShow -> tvShowDetailDtoToDomainMapper.transform(this)
    }
}
