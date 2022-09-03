package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.app.media.domain.model.Picture
import com.raxdenstudios.app.media.domain.model.Size
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.di.APIVersionV3
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

internal class PictureDtoToDomainMapper @Inject constructor(
    @APIVersionV3 private val apiDataProvider: APIDataProvider,
) : DataMapper<String?, Picture>() {

    override fun transform(source: String?): Picture =
        source?.takeIf { it.isNotEmpty() }?.let {
            Picture.WithImage(
                thumbnail = Size.Thumbnail(apiDataProvider.getImageDomain(), source),
                original = Size.Original(apiDataProvider.getImageDomain(), source),
            )
        } ?: Picture.Empty
}
