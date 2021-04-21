package com.raxdenstudios.app.movie.data.local.mapper

import com.raxdenstudios.app.movie.data.local.model.MediaEntity
import com.raxdenstudios.app.movie.data.local.model.PictureEntity
import com.raxdenstudios.app.movie.data.local.model.SizeEntity
import com.raxdenstudios.app.movie.data.local.model.VoteEntity
import com.raxdenstudios.app.movie.domain.model.*
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.commons.threeten.ext.toLocalDate
import com.raxdenstudios.commons.threeten.ext.toMilliseconds
import com.raxdenstudios.commons.util.DataMapper

internal class MediaToEntityMapper(
  private val pictureToEntityMapper: PictureToEntityMapper,
  private val voteToEntityMapper: VoteToEntityMapper,
) : DataMapper<Media, MediaEntity>() {

  override fun transform(source: Media): MediaEntity = MediaEntity(
    id = source.id,
    title = source.title,
    backdrop = pictureToEntityMapper.transform(source.backdrop),
    poster = pictureToEntityMapper.transform(source.poster),
    release = source.release.toMilliseconds(),
    vote = voteToEntityMapper.transform(source.vote),
    watchList = source.watchList,
  )
}

internal class MediaEntityToDomainMapper(
  private val pictureEntityToDomainMapper: PictureEntityToDomainMapper,
  private val voteEntityToDomainMapper: VoteEntityToDomainMapper,
) : DataMapper<MediaEntity, Media>() {

  override fun transform(source: MediaEntity): Media = Media(
    id = source.id,
    mediaType = MediaType.Movie,
    title = source.title,
    backdrop = pictureEntityToDomainMapper.transform(source.backdrop),
    poster = pictureEntityToDomainMapper.transform(source.poster),
    release = source.release.toLocalDate(),
    vote = voteEntityToDomainMapper.transform(source.vote),
    watchList = source.watchList,
  )
}

internal class VoteToEntityMapper : DataMapper<Vote, VoteEntity>() {

  override fun transform(source: Vote): VoteEntity = VoteEntity(
    average = source.average,
    count = source.count,
  )
}

internal class VoteEntityToDomainMapper : DataMapper<VoteEntity, Vote>() {

  override fun transform(source: VoteEntity): Vote = Vote(
    average = source.average,
    count = source.count,
  )
}

internal class PictureToEntityMapper(
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

internal class PictureEntityToDomainMapper(
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

internal class SizeToEntityMapper : DataMapper<Size, SizeEntity>() {

  override fun transform(source: Size): SizeEntity = when (source) {
    is Size.Original -> SizeEntity(source.source, "original")
    is Size.Thumbnail -> SizeEntity(source.source, "thumbnail")
  }
}

internal class SizeEntityToDomainMapper(
  private val apiDataProvider: APIDataProvider
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
