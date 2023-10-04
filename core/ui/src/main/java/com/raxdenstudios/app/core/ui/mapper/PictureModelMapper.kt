package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject

class PictureModelMapper @Inject constructor() : DataMapper<Picture, String>() {

    override fun transform(source: Picture): String = when (source) {
        is Picture.Empty -> ""
        is Picture.WithImage -> source.thumbnail.url
    }
}
