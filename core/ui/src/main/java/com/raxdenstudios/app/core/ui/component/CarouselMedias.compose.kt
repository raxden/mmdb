package com.raxdenstudios.app.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun CarouselMedias(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, top = 4.dp, bottom = 16.dp),
    horizontalArrangement: Arrangement.HorizontalOrVertical = Arrangement.spacedBy(4.dp),
    medias: List<MediaModel>,
    onItemClick: (MediaModel) -> Unit = {},
    onItemWatchButtonClick: (MediaModel) -> Unit = {}
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        contentPadding = contentPadding,
        horizontalArrangement = horizontalArrangement,
        content = {
            items(
                count = medias.size,
                key = { index -> medias[index].id.value },
            ) { index ->
                Media(
                    modifier = Modifier,
                    model = medias[index],
                    onClick = { onItemClick(medias[index]) },
                    onWatchButtonClick = { onItemWatchButtonClick(medias[index]) }
                )
            }
        }
    )
}

@DevicePreviews
@Composable
fun CarouselMediasPreview() {
    AppComposeTheme {
        CarouselMedias(
            medias = items,
        )
    }
}

@SuppressLint("VisibleForTests")
@SuppressWarnings("MagicNumber")
private val items = List(10) {
    MediaModel.mock.copy(
        id = MediaId(it.toLong()),
        title = "The Batman",
        releaseYear = "2011",
        rating = "7.8",
        poster = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png"
    )
}
