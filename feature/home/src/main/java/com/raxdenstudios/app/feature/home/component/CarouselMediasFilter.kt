package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun CarouselMediasFilter(
    modifier: Modifier = Modifier,
    filters: List<MediaFilterModel>,
    onclick: (MediaFilterModel) -> Unit = {},
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        filters.forEach { filter ->
            MediaTypeFilterChip(
                label = filter.label,
                isSelected = filter.isSelected,
                onclick = { onclick(filter) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselMediasFilterPreview() {
    AppComposeTheme {
        CarouselMediasFilter(
            filters = listOf(
                MediaFilterModel(
                    id = MediaType.Movie,
                    label = "Movies",
                    isSelected = true,
                ),
                MediaFilterModel(
                    id = MediaType.TvShow,
                    label = "Series",
                    isSelected = false,
                ),
            ),
        )
    }
}
