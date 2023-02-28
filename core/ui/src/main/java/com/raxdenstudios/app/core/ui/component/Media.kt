package com.raxdenstudios.app.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Shapes
import com.raxdenstudios.app.core.ui.theme.Typography

private const val ASPECT_RATIO = 9f / 18f

@Composable
fun Media(
    modifier: Modifier = Modifier,
    model: MediaModel,
    onClick: () -> Unit = {},
    onWatchButtonClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .aspectRatio(ratio = ASPECT_RATIO),
        elevation = 4.dp,
        shape = Shapes.small,
        backgroundColor = MaterialTheme.colors.surface,
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() },
        ) {
            PosterWithWatchButton(
                modifier = Modifier
                    .weight(1f),
                image = model.poster,
                watchlist = model.watchlist,
                onWatchButtonClick = onWatchButtonClick
            )
            Text(
                style = Typography.body2,
                text = model.title,
                maxLines = 1,
                modifier = Modifier
                    .padding(start = 8.dp, end = 8.dp, top = 8.dp)
                    .fillMaxWidth(),
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(8.dp)
            ) {
                RatingImage(
                    modifier = Modifier
                        .size(20.dp)
                )
                Text(
                    style = Typography.caption,
                    text = model.rating,
                    modifier = Modifier
                        .padding(start = 2.dp)
                        .wrapContentHeight()
                )
                Text(
                    style = Typography.caption,
                    text = model.releaseDate,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentWidth(Alignment.End)
                )
            }
        }
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaPreview() {
    AppComposeTheme {
        Media(
            model = MediaModel.mock
        )
    }
}
