package com.raxdenstudios.app.core.ui.component

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.commons.pagination.ext.toPageIndex
import com.raxdenstudios.commons.pagination.model.PageIndex

@SuppressWarnings("LongParameterList")
@Composable
fun MediaGrid(
    modifier: Modifier = Modifier,
    items: List<MediaModel>,
    scrollState: LazyGridState = rememberLazyGridState(),
    onPageIndexListener: (PageIndex) -> Unit = {},
    onItemClick: (MediaModel) -> Unit = {},
    onItemWatchClick: (MediaModel) -> Unit = {}
) {
    Box(
        modifier = modifier
    ) {
        // We use a remembered derived state to minimize unnecessary compositions,
        // due to call layoutInfo implies a recomposition
        val index by remember {
            derivedStateOf {
                scrollState.toPageIndex()
            }
        }
        onPageIndexListener(index)

        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            state = scrollState,
            columns = GridCells.Adaptive(150.dp),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            content = {
                items(
                    count = items.size,
                    key = { index -> items[index].id.value },
                ) { index ->
                    Media(
                        modifier = Modifier,
                        model = items[index],
                        onClick = { onItemClick(items[index]) },
                        onWatchButtonClick = { onItemWatchClick(items[index]) }
                    )
                }
            },
        )
    }
}

@SuppressLint("VisibleForTests")
@SuppressWarnings("MagicNumber")
@DevicePreviews
@Composable
fun MediaGridPreview() {
    AppComposeTheme {
        MediaGrid(
            items = listOf(
                MediaModel.mock
            ),
        )
    }
}
