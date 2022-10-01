package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.model.WatchButtonModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PosterImage(
    modifier: Modifier = Modifier,
    image: String,
    shape: Shape = RoundedCornerShape(0.dp),
    elevation: Dp = 0.dp,
    watchButton: WatchButtonModel? = null,
    onWatchButtonClick: () -> Unit = {},
) {
    Surface(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
        ) {
            CoilImage(
                modifier = Modifier
                    .fillMaxSize(),
                imageModel = { image },
                imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                previewPlaceholder = AppIcons.PreviewPoster,
            )
            if (watchButton != null) {
                WatchButton(
                    model = watchButton,
                    onClick = { onWatchButtonClick() })
            }
        }
    }
}

@DevicePreviews
@Composable
fun PosterImageWithWatchButtonPreview() {
    AppComposeTheme {
        PosterImage(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
            watchButton = WatchButtonModel.Unselected,
        )
    }
}

@DevicePreviews
@Composable
fun PosterImagePreview() {
    AppComposeTheme {
        PosterImage(image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png")
    }
}
