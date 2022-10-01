package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.component.MediaListItem
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme


@Composable
fun CarouselMedias(
    modifier: Modifier = Modifier,
    items: List<MediaModel>,
    onItemClick: (MediaModel) -> Unit = {},
    onItemWatchButtonClick: (MediaModel) -> Unit = {}
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height(300.dp),
        contentPadding = PaddingValues(all =  8.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp),
        content = {
            items(
                count = items.size,
                key = { index -> items[index].id.value },
            ) { index ->
                MediaListItem(
                    modifier = Modifier,
                    model = items[index],
                    onClick = { onItemClick(items[index]) },
                    onWatchButtonClick = { onItemWatchButtonClick(items[index]) }
                )
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun CarouselMediasPreview() {
    AppComposeTheme {
        CarouselMedias(
            items = items,
        )
    }
}

@SuppressWarnings("MagicNumber")
private val items = List(10) {
    MediaModel.empty.copy(
        id = MediaId(it.toLong()),
        title = "The Batman",
        releaseDate = "2011",
        rating = "7.8",
        image = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png"
    )
}
