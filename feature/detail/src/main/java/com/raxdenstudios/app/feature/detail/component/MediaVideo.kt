package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.Video
import com.raxdenstudios.app.core.ui.component.aspectRatio169
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography
import com.raxdenstudios.app.feature.detail.model.VideoModel

@Composable
fun MediaVideo(
    modifier: Modifier = Modifier,
    video: VideoModel,
    onClick: (VideoModel) -> Unit = {},
) {
    Column(
        modifier = modifier
            .clickable { onClick(video) }
            .width(220.dp),
    ) {
        Video(
            modifier = Modifier
                .width(220.dp)
                .aspectRatio169(),
            image = video.thumbnail,
            onPlayClick = { onClick(video) },
        )
        Text(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp),
            style = Typography.body2,
            text = video.name,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaVideoPreview() {
    AppComposeTheme {
        MediaVideo(
            video = VideoModel.mock,
        )
    }
}
