package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

private const val CONTENT_DESCRIPTION = "Rating"

@Composable
fun RatingImage(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = AppIcons.Rating),
        contentDescription = CONTENT_DESCRIPTION,
        modifier = modifier,
    )
}

@DevicePreviews
@Composable
fun RatingImagePreview() {
    AppComposeTheme {
        RatingImage()
    }
}
