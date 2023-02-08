package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.detail.model.VideoModel

@Composable
fun MediaVideos(
    modifier: Modifier = Modifier,
    videos: List<VideoModel>,
    onVideoClick: (VideoModel) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Header(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp),
            title = stringResource(id = R.string.videos),
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
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
