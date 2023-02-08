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
fun Video(
    modifier: Modifier = Modifier,
    elevation: Dp = 0.dp,
    shape: Shape = RoundedCornerShape(0.dp),
    image: String,
    onPlayClick: () -> Unit = {},
    previewPlaceholder: Int = PreviewPlaceHolders.Backdrop
) {
    Surface(
        modifier = modifier,
        elevation = elevation,
        shape = shape,
    ) {
        Box(
            modifier = Modifier
        ) {
            AppVideoImage(
                modifier = Modifier
                    .fillMaxSize(),
                onPlayClick = onPlayClick,
                image = image,
                previewPlaceholder = previewPlaceholder,
            )
        }
    }
}

@DevicePreviews
@Composable
fun VideoPreview() {
    AppComposeTheme {
        Video(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
        )
    }
}
