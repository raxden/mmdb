package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.AppImage
import com.raxdenstudios.app.core.ui.component.Poster
import com.raxdenstudios.app.core.ui.component.aspectRatio169
import com.raxdenstudios.app.core.ui.component.aspectRatio23
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Shapes

private const val PERCENT_3F = .3f
private const val PERCENT_7F = .7f

@Composable
fun MediaHeader(
    modifier: Modifier = Modifier,
    scrollState: ScrollState = rememberScrollState(),
    media: MediaModel
) {
    Surface(
        modifier = modifier
            .semantics { contentDescription = "Media Header" }
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        shape = Shapes.medium,
        elevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY = calculateTranslationY(scrollState)
                    alpha = calculateAlpha(scrollState)
                }
        ) {
            val constraintSet = ConstraintSet {
                val poster = createRefFor("poster")
                val backdrop = createRefFor("backdrop")
                val info = createRefFor("info")

                constrain(poster) {
                    bottom.linkTo(info.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(info.start)
                }

                constrain(info) {
                    start.linkTo(poster.end)
                    top.linkTo(backdrop.bottom)
                    end.linkTo(backdrop.end)
                }
            }
            ConstraintLayout(
                modifier = modifier
                    .fillMaxWidth(),
                constraintSet = constraintSet
            ) {
                AppImage(
                    modifier = Modifier
                        .layoutId("backdrop")
                        .aspectRatio169(),
                    image = media.backdrop,
                )
                Poster(
                    modifier = Modifier
                        .fillMaxWidth(PERCENT_3F)
                        .layoutId("poster")
                        .padding(start = 16.dp, bottom = 16.dp)
                        .width(100.dp)
                        .aspectRatio23(),
                    elevation = 4.dp,
                    shape = Shapes.small,
                    image = media.poster,
                )
                MediaHeaderInfo(
                    modifier = Modifier
                        .fillMaxWidth(PERCENT_7F)
                        .layoutId("info"),
                    media = media,
                )
            }
        }

    }
}

@Suppress("MagicNumber")
private fun calculateAlpha(scrollState: ScrollState) =
    1f - ((scrollState.value.toFloat() / scrollState.maxValue) * 1.5f)

@Suppress("MagicNumber")
private fun calculateTranslationY(scrollState: ScrollState) =
    0.5f * scrollState.value

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaHeaderPreview() {
    AppComposeTheme {
        MediaHeader(
            media = MediaModel.mock,
        )
    }
}
