package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.app.feature.detail.model.VideoModel

@Composable
fun MediaBody(
    modifier: Modifier = Modifier,
    media: MediaModel,
    videos: List<VideoModel>,
    onVideoClick: (VideoModel) -> Unit = {},
    relatedMedias: RelatedMediasModel,
    onRelatedMediaClick: (MediaModel) -> Unit = {},
    onRelatedMediaWatchButtonClick: (MediaModel) -> Unit = {},
    onRelatedSeeAllClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .semantics { contentDescription = "Media Body" }
            .fillMaxWidth()
            .background(color = MaterialTheme.colors.background),
    ) {
        MediaOverview(media = media)
        MediaVideos(
            modifier = Modifier,
            videos = videos,
            onVideoClick = onVideoClick
        )
        RelatedMedias(
            modifier = Modifier,
            relatedMedias = relatedMedias,
            onItemClick = { item -> onRelatedMediaClick(item) },
            onItemWatchButtonClick = { item -> onRelatedMediaWatchButtonClick(item) },
            onSeeAllClick = onRelatedSeeAllClick,
        )
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaBodyPreview() {
    AppComposeTheme {
        MediaBody(
            media = MediaModel.mock,
            videos = listOf(
                VideoModel.mock,
                VideoModel.mock,
            ),
            relatedMedias = RelatedMediasModel.mock,
        )
    }
}
