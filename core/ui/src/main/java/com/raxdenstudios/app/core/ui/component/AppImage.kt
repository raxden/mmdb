package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.icon.PreviewPlaceHolders
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.coil.CoilImage

private const val PLAY_ICON_MAX_SIZE = 0.5f

@Composable
fun AppImage(
    modifier: Modifier = Modifier,
    image: String,
    imageOptions: ImageOptions = ImageOptions(contentScale = ContentScale.Crop),
    previewPlaceholder: Int = PreviewPlaceHolders.Backdrop
) {
    CoilImage(
        modifier = modifier
            .fillMaxSize(),
        imageModel = { image },
        imageOptions = imageOptions,
        previewPlaceholder = previewPlaceholder,
    )
}

@Composable
fun AppVideoImage(
    modifier: Modifier = Modifier,
    image: String,
    onPlayClick: () -> Unit = {},
    imageOptions: ImageOptions = ImageOptions(contentScale = ContentScale.Crop),
    previewPlaceholder: Int = PreviewPlaceHolders.Backdrop
) {
    Box(
        modifier = modifier
    ) {
        CoilImage(
            modifier = Modifier
                .fillMaxSize(),
            imageModel = { image },
            imageOptions = imageOptions,
            previewPlaceholder = previewPlaceholder,
        )
        Icon(
            modifier = Modifier
                .clickable { onPlayClick() }
                .fillMaxSize(PLAY_ICON_MAX_SIZE)
                .align(alignment = Alignment.Center),
            imageVector = AppIcons.Play,
            tint = Color.White,
            contentDescription = null
        )
    }
}

@DevicePreviews
@Composable
fun AppImagePreview() {
    AppComposeTheme {
        AppImage(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
        )
    }
}

@DevicePreviews
@Composable
fun AppVideoImagePreview() {
    AppComposeTheme {
        AppVideoImage(
            image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png",
        )
    }
}
