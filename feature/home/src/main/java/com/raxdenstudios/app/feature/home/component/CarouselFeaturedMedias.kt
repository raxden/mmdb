package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.unit.IntOffset
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalPagerApi::class)
@Composable
fun CarouselFeaturedMedias(
    modifier: Modifier = Modifier,
    items: List<MediaModel>,
    onItemClick: (MediaModel) -> Unit = {},
    onItemWatchButtonClick: (MediaModel) -> Unit = {}
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        count = items.size,
        key = { index -> items[index].id.value },
        content = { page ->

            val width = remember { MutableStateFlow(0) }

            FeaturedMediaListItem(
                modifier = Modifier
                    .onGloballyPositioned { coordinates ->
                        width.value = coordinates.size.width
                    },
                childOffset = {
                    val pageOffset = -calculateCurrentOffsetForPage(page)
                    val xOffset = (width.value * pageOffset).toInt()
                    IntOffset(x = xOffset, y = 0)
                },
                model = items[page],
                onClick = { onItemClick(items[page]) },
                onWatchButtonClick = { onItemWatchButtonClick(items[page]) }
            )
        }
    )
}

@DevicePreviews
@Composable
fun CarouselFeaturedMediasPreview() {
    AppComposeTheme {
        CarouselFeaturedMedias(
            items = items,
        )
    }
}

@SuppressWarnings("MagicNumber")
private val items = List(10) {
    MediaModel.empty.copy(
        id = MediaId(it.toLong()),
        title = "The Batman",
        overview = "The rise of Sacha Baron Cohen",
        releaseYear = "2011",
        rating = "7.8",
        poster = "https://developer.android.com/static/images/jetpack/compose-tutorial/profile_picture.png"
    )
}
