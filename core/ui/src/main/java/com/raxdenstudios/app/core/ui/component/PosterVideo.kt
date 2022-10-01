package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

@Composable
fun PosterVideo(
    modifier: Modifier = Modifier,
    image: String,
    onPlayClick: () -> Unit = {},
) {
    Box(
        modifier = modifier
    ) {
        CoilImage(
            modifier = Modifier
                .fillMaxSize(),
            imageModel = { image },
            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
            previewPlaceholder = AppIcons.PreviewPoster,
        )
        Icon(
            modifier = Modifier
                .clickable { onPlayClick() }
                .size(80.dp)
                .align(alignment = Alignment.Center),
            imageVector = AppIcons.Play,
            tint = Color.White,
            contentDescription = null
        )
    }
}

@DevicePreviews
@Composable
fun PosterVideoPreview() {
    AppComposeTheme {
        PosterVideo(image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png")
    }
}
