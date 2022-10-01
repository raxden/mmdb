package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension.Companion.fillToConstraints
import com.raxdenstudios.app.core.ui.component.PosterImage
import com.raxdenstudios.app.core.ui.component.PosterVideo
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography

private const val VIDEO_ASPECT_RATIO = 16f / 9f
private const val POSTER_ASPECT_RATIO = 9f / 14f

@Composable
fun FeaturedMediaListItem(
    modifier: Modifier = Modifier,
    model: MediaModel,
    childOffset: Density.() -> IntOffset = { IntOffset.Zero },
    onClick: () -> Unit = {},
    onPlayClick: () -> Unit = {},
    onWatchButtonClick: () -> Unit = {},
) {
    BoxWithConstraints {
        val constraintSet = ConstraintSet {
            val posterVideo = createRefFor("posterVideo")
            val poster = createRefFor("poster")
            val title = createRefFor("title")
            val description = createRefFor("description")

            constrain(posterVideo) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
            constrain(poster) {
                bottom.linkTo(description.bottom)
                start.linkTo(parent.start)
            }
            constrain(title) {
                top.linkTo(posterVideo.bottom)
                start.linkTo(poster.end)
                end.linkTo(parent.end)
                width = fillToConstraints
            }
            constrain(description) {
                top.linkTo(title.bottom)
                start.linkTo(title.start)
                end.linkTo(title.end)
                width = fillToConstraints
            }
        }

        ConstraintLayout(
            modifier = modifier
                .fillMaxWidth(),
            constraintSet = constraintSet
        ) {
            PosterVideo(
                modifier = Modifier
                    .clickable(onClick = onClick)
                    .layoutId("posterVideo")
                    .fillMaxWidth()
                    .aspectRatio(VIDEO_ASPECT_RATIO),
                image = model.backdrop,
                onPlayClick = onPlayClick,
            )
            Text(
                modifier = Modifier
                    .layoutId("title")
                    .padding(start = 8.dp, end = 8.dp, top = 16.dp)
                    .offset(childOffset),
                style = Typography.body2,
                text = model.title,
                maxLines = 1,
            )
            Text(
                modifier = Modifier
                    .layoutId("description")
                    .padding(start = 8.dp, end = 8.dp, bottom = 16.dp)
                    .offset(childOffset),
                maxLines = 2,
                style = Typography.caption.copy(color = Color.Gray),
                text = model.description,
            )
            PosterImage(
                modifier = Modifier
                    .layoutId("poster")
                    .clickable { onClick() }
                    .padding(start = 20.dp, top = 8.dp, bottom = 16.dp, end = 8.dp)
                    .offset(childOffset)
                    .width(100.dp)
                    .aspectRatio(POSTER_ASPECT_RATIO),
                elevation = 4.dp,
                shape = RoundedCornerShape(8.dp),
                image = model.image,
                watchButton = model.watchButton,
                onWatchButtonClick = onWatchButtonClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FeaturedMediaListItemPreview() {
    AppComposeTheme {
        FeaturedMediaListItem(
            model = MediaModel.empty.copy(
                title = "The Batman",
                description = "The rise of Sacha Baron Cohen",
                releaseDate = "2011",
                rating = "7.8",
                image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png"
            )
        )
    }
}
