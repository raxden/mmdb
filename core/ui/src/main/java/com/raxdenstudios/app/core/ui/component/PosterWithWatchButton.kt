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
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun PosterWithWatchButton(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(0.dp),
    image: String,
    watchlist: Boolean = false,
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
            WatchButton(
                isSelected = watchlist,
                onClick = { onWatchButtonClick() })
        }
    }
}

@DevicePreviews
@Composable
fun PosterWithWatchButtonPreview() {
    AppComposeTheme {
        PosterWithWatchButton(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
            watchlist = false,
        )
    }
}
