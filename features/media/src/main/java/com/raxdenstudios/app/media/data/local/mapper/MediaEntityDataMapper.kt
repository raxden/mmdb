package com.raxdenstudios.app.media.data.local.mapper

import com.raxdenstudios.app.media.data.local.model.MediaEntity
import com.raxdenstudios.app.media.data.local.model.PictureEntity
import com.raxdenstudios.app.media.data.local.model.SizeEntity
import com.raxdenstudios.app.media.data.local.model.VoteEntity
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.domain.model.Picture
import com.raxdenstudios.app.media.domain.model.Size
import com.raxdenstudios.app.media.domain.model.Vote
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.di.APIVersionV3
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.threeten.ext.toLocalDate
import com.raxdenstudios.commons.threeten.ext.toMilliseconds
import javax.inject.Inject

internal class MediaToEntityMapper @Inject constructor(
  private val pictureToEntityMapper: PictureToEntityMapper,
  private val voteToEntityMapper: VoteToEntityMapper,
) : DataMapper<Media, MediaEntity>() {

  override fun transform(source: Media): MediaEntity = when (source) {
    is Media.Movie -> MediaEntity(
      id = source.id.value,
      type = MediaType.MOVIE.ordinal,
      title = source.title,
      backdrop = pictureToEntityMapper.transform(source.backdrop),
      poster = pictureToEntityMapper.transform(source.poster),
      release = source.release.toMilliseconds(),
      vote = voteToEntityMapper.transform(source.vote),
    )
    is Media.TVShow -> MediaEntity(
      id = source.id.value,
      type = MediaType.TV_SHOW.ordinal,
      title = source.name,
      backdrop = pictureToEntityMapper.transform(source.backdrop),
      poster = pictureToEntityMapper.transform(source.poster),
      release = source.firstAirDate.toMilliseconds(),
      vote = voteToEntityMapper.transform(source.vote),
    )
  }
}

internal class MediaEntityToDomainMapper @Inject constructor(
  private val pictureEntityToDomainMapper: PictureEntityToDomainMapper,
  private val voteEntityToDomainMapper: VoteEntityToDomainMapper,
) : DataMapper<MediaEntity, Media>() {

  override fun transform(source: MediaEntity): Media = when (source.type) {
    MediaType.MOVIE.ordinal -> Media.Movie(
      id = MediaId(source.id),
      title = source.title,
      backdrop = pictureEntityToDomainMapper.transform(source.backdrop),
      poster = pictureEntityToDomainMapper.transform(source.poster),
      release = source.release.toLocalDate(),
      vote = voteEntityToDomainMapper.transform(source.vote),
      watchList = false,
    )
    MediaType.TV_SHOW.ordinal -> Media.TVShow(
      id = MediaId(source.id),
      name = source.title,
      backdrop = pictureEntityToDomainMapper.transform(source.backdrop),
      poster = pictureEntityToDomainMapper.transform(source.poster),
      firstAirDate = source.release.toLocalDate(),
      vote = voteEntityToDomainMapper.transform(source.vote),
      watchList = false,
    )
    else -> throw IllegalStateException("Invalid type stored in database, ${source.type}")
  }
}

internal class VoteToEntityMapper @Inject constructor() : DataMapper<Vote, VoteEntity>() {

  override fun transform(source: Vote): VoteEntity = VoteEntity(
    average = source.average,
    count = source.count,
  )
}

internal class VoteEntityToDomainMapper @Inject constructor() : DataMapper<VoteEntity, Vote>() {

  override fun transform(source: VoteEntity): Vote = Vote(
    average = source.average,
    count = source.count,
  )
}

internal class PictureToEntityMapper @Inject constructor(
  private val sizeToEntityMapper: SizeToEntityMapper
) : DataMapper<Picture, PictureEntity>() {

  override fun transform(source: Picture): PictureEntity = when (source) {
    Picture.Empty -> PictureEntity.empty
    is Picture.WithImage -> PictureEntity(
      thumbnail = sizeToEntityMapper.transform(source.thumbnail),
      original = sizeToEntityMapper.transform(source.original),
    )
  }
}

internal class PictureEntityToDomainMapper @Inject constructor(
  private val sizeEntityToDomainMapper: SizeEntityToDomainMapper
) : DataMapper<PictureEntity?, Picture>() {

  override fun transform(source: PictureEntity?): Picture =
    source?.takeIf { it.thumbnail.url.isNotEmpty() && it.original.url.isNotEmpty() }?.let {
      Picture.WithImage(
        thumbnail = sizeEntityToDomainMapper.transform(source.thumbnail) as Size.Thumbnail,
        original = sizeEntityToDomainMapper.transform(source.original) as Size.Original,
      )
    } ?: Picture.Empty
}

internal class SizeToEntityMapper @Inject constructor() : DataMapper<Size, SizeEntity>() {

  override fun transform(source: Size): SizeEntity = when (source) {
    is Size.Original -> SizeEntity(source.source, "original")
    is Size.Thumbnail -> SizeEntity(source.source, "thumbnail")
  }
}

internal class SizeEntityToDomainMapper @Inject constructor(
  @APIVersionV3 private val apiDataProvider: APIDataProvider
) : DataMapper<SizeEntity, Size>() {

  override fun transform(source: SizeEntity): Size = when (source.type) {
    "thumbnail" -> Size.Thumbnail(
      apiDataProvider.getImageDomain(),
      source.url
    )
    "original" -> Size.Original(
      apiDataProvider.getImageDomain(),
      source.url
    )
    else -> throw IllegalStateException("Invalid field value to represent Picture.Size")
  }
}
