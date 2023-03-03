package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.component.Video
import com.raxdenstudios.app.core.ui.component.aspectRatio169
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography
import com.raxdenstudios.app.feature.detail.model.VideoModel

@Composable
fun MediaVideos(
    modifier: Modifier = Modifier,
    videos: List<VideoModel>,
    onVideoClick: (VideoModel) -> Unit = {},
) {
    if (videos.isEmpty()) return
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Header(
            modifier = Modifier,
            title = stringResource(id = R.string.videos),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(
                    count = videos.size,
                ) { index ->
                    MediaVideo(
                        modifier = Modifier,
                        video = videos[index],
                        onClick = onVideoClick,
                    )
                }
            }
        )
    }
}

@Composable
private fun MediaVideo(
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
fun MediaVideosPreview() {
    AppComposeTheme {
        MediaVideos(
            modifier = Modifier,
            videos = listOf(
                VideoModel.mock,
                VideoModel.mock,
                VideoModel.mock,
                VideoModel.mock,
            ),
        )
    }
}
