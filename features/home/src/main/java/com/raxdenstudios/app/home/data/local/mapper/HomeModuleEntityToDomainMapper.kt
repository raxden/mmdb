package com.raxdenstudios.app.home.data.local.mapper

import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.util.DataMapper

internal class HomeModuleEntityToDomainMapper : DataMapper<HomeModuleEntity, HomeModule>() {

  override fun transform(source: HomeModuleEntity): HomeModule = source.toDomain()

  private fun HomeModuleEntity.toDomain(): HomeModule = when (type) {
    1 -> when (subtype) {
      1 -> HomeModule.Popular(MediaType.MOVIE)
      2 -> HomeModule.NowPlaying(MediaType.MOVIE)
      3 -> HomeModule.TopRated(MediaType.MOVIE)
      4 -> HomeModule.Upcoming
      5 -> HomeModule.WatchList(MediaType.MOVIE)
      else -> throw IllegalStateException("module subtype with value $subtype doesn't exists")
    }
    else -> throw IllegalStateException("module type with value $type doesn't exists")
  }
}
