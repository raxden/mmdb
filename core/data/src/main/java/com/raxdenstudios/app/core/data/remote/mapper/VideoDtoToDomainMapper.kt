package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.model.Size
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.model.VideoType
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class VideoDtoToDomainMapper @Inject constructor(
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper,
) : DataMapper<VideoDto, Video>() {

    override fun transform(source: VideoDto): Video = Video(
        uri = when (source.site) {
            "YouTube" -> "https://www.youtube.com/watch?v=${source.key}"
            else -> ""
        },
        name = source.name,
        site = source.site,
        size = source.size,
        type = when (source.type) {
            "Trailer" -> VideoType.Trailer
            "Clip" -> VideoType.Clip
            "Behind the Scenes" -> VideoType.BehindTheScenes
            else -> error("VideoType not found: ${source.type}")
        },
        official = source.official,
        published = dateDtoToLocalDateMapper.transform(source.published_at),
        picture = when (source.site) {
            "YouTube" -> Picture.WithImage(
                thumbnail = Size.Thumbnail("https://img.youtube.com/vi/${source.key}/hqdefault.jpg"),
                original = Size.Original("https://img.youtube.com/vi/${source.key}/maxresdefault.jpg"),
            )
            else -> Picture.Empty
        },
    )
}
