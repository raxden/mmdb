package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.model.Size
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.di.APIVersionV3
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class PictureDtoToDomainMapper @Inject constructor(
    @APIVersionV3 private val apiDataProvider: APIDataProvider,
) : DataMapper<String?, Picture>() {

    override fun transform(source: String?): Picture =
        source?.takeIf { it.isNotEmpty() }?.let {
            Picture.WithImage(
                thumbnail = Size.Thumbnail(apiDataProvider.baseImageUrl, source),
                original = Size.Original(apiDataProvider.baseImageUrl, source),
            )
        } ?: Picture.Empty
}
