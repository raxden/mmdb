package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.database.model.MediaEntity
import com.raxdenstudios.app.core.database.model.PictureEntity
import com.raxdenstudios.app.core.database.model.SizeEntity
import com.raxdenstudios.app.core.database.model.VoteEntity
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.model.Size
import com.raxdenstudios.app.core.model.Vote
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.threeten.ext.toLocalDate
import com.raxdenstudios.commons.threeten.ext.toMilliseconds
import org.threeten.bp.Duration
import javax.inject.Inject

class MediaToEntityMapper @Inject constructor(
    private val pictureToEntityMapper: PictureToEntityMapper,
    private val voteToEntityMapper: VoteToEntityMapper,
) : DataMapper<Media, MediaEntity>() {

    override fun transform(source: Media): MediaEntity = when (source) {
        is Media.Movie -> MediaEntity(
            id = source.id.value,
            type = MediaType.Movie.value,
            title = source.title,
            overview = source.overview,
            backdrop = pictureToEntityMapper.transform(source.backdrop),
            poster = pictureToEntityMapper.transform(source.poster),
            release = source.release.toMilliseconds(),
            vote = voteToEntityMapper.transform(source.vote),
        )
        is Media.TVShow -> MediaEntity(
            id = source.id.value,
            type = MediaType.TvShow.value,
            title = source.name,
            overview = source.overview,
            backdrop = pictureToEntityMapper.transform(source.backdrop),
            poster = pictureToEntityMapper.transform(source.poster),
            release = source.firstAirDate.toMilliseconds(),
            vote = voteToEntityMapper.transform(source.vote),
        )
    }
}

class MediaEntityToDomainMapper @Inject constructor(
    private val pictureEntityToDomainMapper: PictureEntityToDomainMapper,
    private val voteEntityToDomainMapper: VoteEntityToDomainMapper,
) : DataMapper<MediaEntity, Media>() {

    override fun transform(source: MediaEntity): Media = when (source.type) {
        MediaType.Movie.value -> Media.Movie(
            id = MediaId(source.id),
            title = source.title,
            overview = source.overview,
            backdrop = pictureEntityToDomainMapper.transform(source.backdrop),
            poster = pictureEntityToDomainMapper.transform(source.poster),
            release = source.release.toLocalDate(),
            vote = voteEntityToDomainMapper.transform(source.vote),
            watchList = false,
            duration = Duration.ofMinutes(0),
            genres = emptyList(),
        )
        MediaType.TvShow.value -> Media.TVShow(
            id = MediaId(source.id),
            name = source.title,
            overview = source.overview,
            backdrop = pictureEntityToDomainMapper.transform(source.backdrop),
            poster = pictureEntityToDomainMapper.transform(source.poster),
            firstAirDate = source.release.toLocalDate(),
            vote = voteEntityToDomainMapper.transform(source.vote),
            watchList = false,
            duration = Duration.ofMinutes(0),
            genres = emptyList(),
        )
        else -> error("Invalid type stored in database, ${source.type}")
    }
}

class VoteToEntityMapper @Inject constructor() : DataMapper<Vote, VoteEntity>() {

    override fun transform(source: Vote): VoteEntity = VoteEntity(
        average = source.average,
        count = source.count,
    )
}

class VoteEntityToDomainMapper @Inject constructor() : DataMapper<VoteEntity, Vote>() {

    override fun transform(source: VoteEntity): Vote = Vote(
        average = source.average,
        count = source.count,
    )
}

class PictureToEntityMapper @Inject constructor(
    private val sizeToEntityMapper: SizeToEntityMapper,
) : DataMapper<Picture, PictureEntity>() {

    override fun transform(source: Picture): PictureEntity = when (source) {
        Picture.Empty -> PictureEntity.empty
        is Picture.WithImage -> PictureEntity(
            thumbnail = sizeToEntityMapper.transform(source.thumbnail),
            original = sizeToEntityMapper.transform(source.original),
        )
    }
}

class PictureEntityToDomainMapper @Inject constructor(
    private val sizeEntityToDomainMapper: SizeEntityToDomainMapper,
) : DataMapper<PictureEntity?, Picture>() {

    override fun transform(source: PictureEntity?): Picture =
        source?.takeIf { it.thumbnail.url.isNotEmpty() && it.original.url.isNotEmpty() }?.let {
            Picture.WithImage(
                thumbnail = sizeEntityToDomainMapper.transform(source.thumbnail) as Size.Thumbnail,
                original = sizeEntityToDomainMapper.transform(source.original) as Size.Original,
            )
        } ?: Picture.Empty
}

class SizeToEntityMapper @Inject constructor() : DataMapper<Size, SizeEntity>() {

    override fun transform(source: Size): SizeEntity = when (source) {
        is Size.Original -> SizeEntity(source.url, "original")
        is Size.Thumbnail -> SizeEntity(source.url, "thumbnail")
    }
}

class SizeEntityToDomainMapper @Inject constructor() : DataMapper<SizeEntity, Size>() {

    override fun transform(source: SizeEntity): Size = when (source.type) {
        "thumbnail" -> Size.Thumbnail(source.url)
        "original" -> Size.Original(source.url)
        else -> error("Invalid field value to represent Picture.Size")
    }
}
