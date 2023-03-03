package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun MediaOverview(
    modifier: Modifier = Modifier,
    media: MediaModel
) {
    Text(
        modifier = modifier
            .padding(horizontal = 16.dp, vertical = 16.dp),
        style = Typography.body1,
        text = media.overview
    )
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaOverviewPreview() {
    AppComposeTheme {
        MediaOverview(
            media = MediaModel.mock,
        )
    }
}
