package com.raxdenstudios.app.home.data.local.mapper

import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

internal class HomeModuleEntityToDomainMapper @Inject constructor() :
    DataMapper<HomeModuleEntity, HomeModule>() {

    override fun transform(source: HomeModuleEntity): HomeModule = source.toDomain()

    private fun HomeModuleEntity.toDomain(): HomeModule = when (type) {
        1 -> when (subtype) {
            1 -> HomeModule.popularMovies
            2 -> HomeModule.nowPlayingMovies
            3 -> HomeModule.topRatedMovies
            4 -> HomeModule.upcomingMovies
            5 -> HomeModule.watchListMovies
            else -> throw IllegalStateException("module subtype with value $subtype doesn't exists")
        }
        else -> throw IllegalStateException("module type with value $type doesn't exists")
    }
}
