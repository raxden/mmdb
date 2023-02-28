package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.CarouselMedias
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel

@Composable
fun RelatedMedias(
    modifier: Modifier = Modifier,
    relatedMedias: RelatedMediasModel,
    onItemClick: (MediaModel) -> Unit = {},
    onItemWatchButtonClick: (MediaModel) -> Unit = {},
    onSeeAllClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Header(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 4.dp),
            title = relatedMedias.label,
            seeAllVisible = true,
            onSeeAllClick = onSeeAllClick,
        )
        CarouselMedias(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            medias = relatedMedias.medias,
            onItemClick = { item -> onItemClick(item) },
            onItemWatchButtonClick = { item -> onItemWatchButtonClick(item) },
        )
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaRelatedPreview() {
    AppComposeTheme {
        RelatedMedias(
            modifier = Modifier,
            relatedMedias = RelatedMediasModel.mock,
        )
    }
}
