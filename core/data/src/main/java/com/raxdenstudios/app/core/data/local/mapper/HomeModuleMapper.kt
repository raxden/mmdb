package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.database.model.HomeModuleEntity
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject

class HomeModuleToEntityMapper @Inject constructor() :
    DataMapper<HomeModule, HomeModuleEntity>() {

    override fun transform(source: HomeModule): HomeModuleEntity = when (source) {
        is HomeModule.Carousel -> when (source.mediaCategory) {
            MediaCategory.NowPlaying -> HomeModuleEntity(
                id = source.id,
                type = HomeModuleEntity.MODULE_MEDIAS,
                mediaType = source.mediaType.value,
                mediaCategory = MediaCategory.NowPlaying.value,
                order = source.order,
            )
            MediaCategory.Popular -> HomeModuleEntity(
                id = source.id,
                type = HomeModuleEntity.MODULE_MEDIAS,
                mediaType = source.mediaType.value,
                mediaCategory = MediaCategory.Popular.value,
                order = source.order,
            )
            MediaCategory.TopRated -> HomeModuleEntity(
                id = source.id,
                type = HomeModuleEntity.MODULE_MEDIAS,
                mediaType = source.mediaType.value,
                mediaCategory = MediaCategory.TopRated.value,
                order = source.order,
            )
            MediaCategory.Upcoming -> HomeModuleEntity(
                id = source.id,
                type = HomeModuleEntity.MODULE_MEDIAS,
                mediaType = source.mediaType.value,
                mediaCategory = MediaCategory.Upcoming.value,
                order = source.order,
            )
            MediaCategory.Watchlist -> HomeModuleEntity(
                id = source.id,
                type = HomeModuleEntity.MODULE_MEDIAS,
                mediaType = source.mediaType.value,
                mediaCategory = MediaCategory.Watchlist.value,
                order = source.order,
            )
        }
        is HomeModule.OtherModule -> error("Unknown module: $this")
    }
}

class HomeModuleEntityToDomainMapper @Inject constructor() :
    DataMapper<HomeModuleEntity, HomeModule>() {

    override fun transform(source: HomeModuleEntity): HomeModule =
        HomeModule.Carousel(
            id = source.id,
            mediaType = MediaType.fromValue(source.mediaType),
            mediaCategory = MediaCategory.fromValue(source.mediaCategory),
            order = source.order,
        )
}
