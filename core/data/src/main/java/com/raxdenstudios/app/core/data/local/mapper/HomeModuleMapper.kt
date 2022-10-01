package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.database.model.HomeModuleEntity
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class HomeModuleToEntityMapper @Inject constructor() :
    DataMapper<HomeModule, HomeModuleEntity>() {

    override fun transform(source: HomeModule): HomeModuleEntity = when (source) {
        is HomeModule.NowPlaying -> HomeModuleEntity(
            id = source.id,
            type = HomeModuleEntity.MODULE_MEDIAS,
            mediaType = source.mediaType.value,
            mediaCategory = MediaCategory.NowPlaying.value,
            order = source.order,
        )
        is HomeModule.Popular -> HomeModuleEntity(
            id = source.id,
            type = HomeModuleEntity.MODULE_MEDIAS,
            mediaType = source.mediaType.value,
            mediaCategory = MediaCategory.Popular.value,
            order = source.order,
        )
        is HomeModule.TopRated -> HomeModuleEntity(
            id = source.id,
            type = HomeModuleEntity.MODULE_MEDIAS,
            mediaType = source.mediaType.value,
            mediaCategory = MediaCategory.TopRated.value,
            order = source.order,
        )
        is HomeModule.Upcoming -> HomeModuleEntity(
            id = source.id,
            type = HomeModuleEntity.MODULE_MEDIAS,
            mediaType = source.mediaType.value,
            mediaCategory = MediaCategory.Upcoming.value,
            order = source.order,
        )
        is HomeModule.Watchlist -> HomeModuleEntity(
            id = source.id,
            type = HomeModuleEntity.MODULE_MEDIAS,
            mediaType = source.mediaType.value,
            mediaCategory = MediaCategory.Watchlist.value,
            order = source.order,
        )
    }
}

class HomeModuleEntityToDomainMapper @Inject constructor() :
    DataMapper<HomeModuleEntity, HomeModule>() {

    override fun transform(source: HomeModuleEntity): HomeModule =
        when (MediaCategory.fromValue(source.mediaCategory)) {
            MediaCategory.Popular -> HomeModule.Popular(
                id = source.id,
                mediaType = MediaType.fromValue(source.mediaType),
                order = source.order,
            )
            MediaCategory.TopRated -> HomeModule.TopRated(
                id = source.id,
                mediaType = MediaType.fromValue(source.mediaType),
                order = source.order,
            )
            MediaCategory.NowPlaying -> HomeModule.NowPlaying(
                id = source.id,
                mediaType = MediaType.fromValue(source.mediaType),
                order = source.order,
            )
            MediaCategory.Upcoming -> HomeModule.Upcoming(
                id = source.id,
                mediaType = MediaType.fromValue(source.mediaType),
                order = source.order,
            )
            MediaCategory.Watchlist -> HomeModule.Watchlist(
                id = source.id,
                mediaType = MediaType.fromValue(source.mediaType),
                order = source.order,
            )
            else -> error("Unknown media category: $this")
        }
}
