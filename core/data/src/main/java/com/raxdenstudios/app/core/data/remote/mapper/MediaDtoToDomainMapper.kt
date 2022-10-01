package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class MediaDtoToDomainMapper @Inject constructor(
    private val movieDtoToDomainMapper: MovieDtoToDomainMapper,
    private val tvShowDtoToDomainMapper: TVShowDtoToDomainMapper,
) : DataMapper<MediaDto, Media>() {

    override fun transform(source: MediaDto): Media = source.toDomain()

    private fun MediaDto.toDomain() = when (this) {
        is MediaDto.Movie -> movieDtoToDomainMapper.transform(this)
        is MediaDto.TVShow -> tvShowDtoToDomainMapper.transform(this)
    }
}
