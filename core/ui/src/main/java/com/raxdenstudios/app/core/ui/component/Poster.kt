package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.PreviewPlaceHolders
import com.raxdenstudios.app.core.ui.model.WatchButtonModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun Poster(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(0.dp),
    image: String,
    watchButton: WatchButtonModel? = null,
    onWatchButtonClick: () -> Unit = {},
    previewPlaceholder: Int = PreviewPlaceHolders.Poster
) {
    Surface(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
        ) {
            AppImage(
                modifier = Modifier
                    .fillMaxSize(),
                image = image,
                previewPlaceholder = previewPlaceholder,
            )
            if (watchButton != null)
                WatchButton(
                    model = watchButton,
                    onClick = { onWatchButtonClick() })
        }
    }
}

@DevicePreviews
@Composable
fun PosterWithWatchButtonPreview() {
    AppComposeTheme {
        Poster(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
            watchButton = WatchButtonModel.Unselected,
        )
    }
}


@DevicePreviews
@Composable
fun PosterPreview() {
    AppComposeTheme {
        Poster(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
        )
    }
}
