package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography

private const val ASPECT_RATIO = 9f / 18f

@Composable
fun MediaListItem(
    modifier: Modifier = Modifier,
    model: MediaModel,
    onClick: () -> Unit = {},
    onWatchButtonClick: () -> Unit = {},
) {
    Card(
        modifier = modifier
            .aspectRatio(ratio = ASPECT_RATIO),
        elevation = 4.dp,
        shape = RoundedCornerShape(8.dp),
        backgroundColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .clickable { onClick() },
        ) {
            PosterImage(
                modifier = Modifier
                    .weight(1f),
                image = model.image,
                watchButton = model.watchButton,
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

@DevicePreviews
@Composable
fun MediaListItemPreview() {
    AppComposeTheme {
        MediaListItem(
            model = MediaModel.empty.copy(
                title = "The Batman",
                releaseDate = "2011",
                rating = "7.8",
                image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png"
            )
        )
    }
}
