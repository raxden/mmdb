package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Grey400
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun MediaHeaderInfo(
    modifier: Modifier = Modifier,
    media: MediaModel,
) {
    Column(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .fillMaxWidth()
    ) {
        HeaderInfoRating(media)
        HeaderInfoTitle(media)
        HeaderInfoGenres(media)
    }
}

@Composable
private fun HeaderInfoGenres(media: MediaModel) {
    Text(
        modifier = Modifier.padding(top = 6.dp),
        style = Typography.body2,
        text = media.genres,
    )
}

@Composable
private fun HeaderInfoTitle(media: MediaModel) {
    Text(
        style = Typography.h6,
        text = media.title,
    )
}

@Composable
private fun HeaderInfoRating(
    media: MediaModel,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (media.certification.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .border(BorderStroke(1.dp, Grey400))
                    .padding(3.dp),
            ) {
                Text(
                    style = Typography.caption,
                    text = media.certification,
                )
            }
            DotSeparator()
        }
        Text(
            style = Typography.caption,
            text = media.releaseDate,
        )
        DotSeparator()
        Text(
            style = Typography.caption,
            text = media.duration,
        )
    }
}

@Composable
private fun DotSeparator() {
    Icon(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(4.dp),
        imageVector = AppIcons.Circle,
        contentDescription = null
    )
}


@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaHeaderInfoPreview() {
    AppComposeTheme {
        MediaHeaderInfo(
            media = MediaModel.mock,
        )
    }
}
