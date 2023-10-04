package com.raxdenstudios.app.feature.detail.mapper

import com.raxdenstudios.app.core.model.Picture
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.feature.detail.model.VideoModel
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject

class VideoModelMapper @Inject constructor() : DataMapper<Video, VideoModel>() {

    override fun transform(source: Video): VideoModel = VideoModel(
        uri = source.uri,
        name = source.name,
        thumbnail = when (val picture = source.picture) {
            Picture.Empty -> ""
            is Picture.WithImage -> picture.thumbnail.url
        }
    )
}
