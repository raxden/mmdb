package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.commons.ext.removeItem
import com.raxdenstudios.commons.ext.replaceItem

sealed class HomeModuleModel {

  sealed class CarouselMedias(
    open val mediaType: MediaType,
    open val hasMediaTypeFilter: Boolean,
  ) : HomeModuleModel() {

    abstract val label: String
    abstract val medias: List<MediaListItemModel>

    fun hasMedias(): Boolean = medias.isNotEmpty()

    abstract fun updateMedia(media: MediaListItemModel): CarouselMedias
    abstract fun copyWith(mediaType: MediaType, medias: List<MediaListItemModel>): CarouselMedias

    data class Popular(
      override val label: String,
      override val medias: List<MediaListItemModel>,
      override val mediaType: MediaType,
    ) : CarouselMedias(mediaType, true) {

      companion object {
        @VisibleForTesting
        val empty = Popular(
          label = "",
          medias = emptyList(),
          mediaType = MediaType.MOVIE,
        )
      }

      override fun copyWith(
        mediaType: MediaType,
        medias: List<MediaListItemModel>
      ): CarouselMedias = copy(mediaType = mediaType, medias = medias)

      override fun updateMedia(media: MediaListItemModel): Popular =
        copy(medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id })
    }

    data class WatchList(
      override val label: String,
      override val medias: List<MediaListItemModel>,
      override val mediaType: MediaType,
    ) : CarouselMedias(mediaType, true) {

      companion object {
        @VisibleForTesting
        val empty = WatchList(
          label = "",
          medias = emptyList(),
          mediaType = MediaType.MOVIE,
        )
      }

      override fun copyWith(
        mediaType: MediaType,
        medias: List<MediaListItemModel>
      ): CarouselMedias = copy(mediaType = mediaType, medias = medias)

      override fun updateMedia(media: MediaListItemModel): WatchList =
        when (media.watchButtonModel) {
          WatchButtonModel.Selected -> addMedia(media)
          WatchButtonModel.Unselected -> removeMedia(media)
        }

      private fun addMedia(media: MediaListItemModel): WatchList = when {
        medias.contains(media) -> this
        else -> copy(medias = medias.toMutableList().apply { add(0, media) }.toList())
      }

      private fun removeMedia(media: MediaListItemModel): WatchList = copy(
        medias = medias.removeItem { mediaToRemove -> mediaToRemove.id == media.id }
      )
    }

    data class NowPlaying(
      override val label: String,
      override val medias: List<MediaListItemModel>,
    ) : CarouselMedias(MediaType.MOVIE, false) {

      companion object {
        @VisibleForTesting
        val empty = NowPlaying(
          label = "",
          medias = emptyList()
        )
      }

      override fun copyWith(
        mediaType: MediaType,
        medias: List<MediaListItemModel>
      ): CarouselMedias = copy(medias = medias)

      override fun updateMedia(media: MediaListItemModel): NowPlaying =
        copy(medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id })
    }

    data class TopRated(
      override val label: String,
      override val medias: List<MediaListItemModel>,
      override val mediaType: MediaType,
    ) : CarouselMedias(mediaType, true) {

      companion object {
        @VisibleForTesting
        val empty = TopRated(
          label = "",
          medias = emptyList(),
          mediaType = MediaType.MOVIE,
        )
      }

      override fun copyWith(
        mediaType: MediaType,
        medias: List<MediaListItemModel>
      ): CarouselMedias = copy(mediaType = mediaType, medias = medias)

      override fun updateMedia(media: MediaListItemModel): TopRated =
        copy(medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id })
    }

    data class Upcoming(
      override val label: String,
      override val medias: List<MediaListItemModel>,
    ) : CarouselMedias(MediaType.MOVIE, false) {

      companion object {
        @VisibleForTesting
        val empty = Upcoming(
          label = "",
          medias = emptyList(),
        )
      }

      override fun copyWith(
        mediaType: MediaType,
        medias: List<MediaListItemModel>
      ): CarouselMedias = copy(medias = medias)

      override fun updateMedia(media: MediaListItemModel): Upcoming =
        copy(medias = medias.replaceItem(media) { mediaToReplace -> mediaToReplace.id == media.id })
    }
  }

  fun getModuleItemId(): Long = when (this) {
    is CarouselMedias.NowPlaying -> 1
    is CarouselMedias.Popular -> 2
    is CarouselMedias.TopRated -> 3
    is CarouselMedias.Upcoming -> 4
    is CarouselMedias.WatchList -> 5
  }
}
