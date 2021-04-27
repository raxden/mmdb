package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.media.view.model.MediaListItemModel

data class HomeModel(
  val logged: Boolean,
  val modules: List<HomeModuleModel>
) {

  fun updateMedia(media: MediaListItemModel): HomeModel = copy(
    modules = modules.map { module ->
      when (module) {
        is HomeModuleModel.CarouselMedias -> module.updateMedia(media)
      }
    }
  )

  companion object {
    @VisibleForTesting
    val empty = HomeModel(
      logged = false,
      modules = emptyList()
    )
  }
}
