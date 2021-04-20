package com.raxdenstudios.app.home.data.local.mapper

import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.commons.util.DataMapper

internal class HomeModuleEntityToDomainMapper : DataMapper<HomeModuleEntity, HomeModule>() {

  override fun transform(source: HomeModuleEntity): HomeModule = source.toDomain()

  private fun HomeModuleEntity.toDomain(): HomeModule = when (type) {
    1 -> when (subtype) {
      1 -> HomeModule.Popular(MediaType.Movie)
      2 -> HomeModule.NowPlaying(MediaType.Movie)
      3 -> HomeModule.TopRated(MediaType.Movie)
      4 -> HomeModule.Upcoming
      5 -> HomeModule.WatchList(MediaType.Movie)
      else -> throw IllegalStateException("module subtype with value $subtype doesn't exists")
    }
    else -> throw IllegalStateException("module type with value $type doesn't exists")
  }
}
