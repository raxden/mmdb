package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.feature.home.component.HomePreviewData.popularModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Shapes

@Composable
fun CarouselMediasModule(
    modifier: Modifier = Modifier,
    module: HomeModuleModel.Carousel,
    onFilterClick: (HomeModuleModel.Carousel, MediaFilterModel) -> Unit = { _, _ -> },
    onSeeAllClick: (HomeModuleModel.Carousel) -> Unit = {},
    onItemClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
    onItemWatchButtonClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
) {
    Surface(
        modifier = modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth(),
        shape = Shapes.large,
        elevation = 4.dp,
    ) {
        Column(
            modifier = Modifier
        ) {
            CarouselMediasHeader(
                modifier = Modifier
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp),
                title = module.label,
                onSeeAllClick = { onSeeAllClick(module) },
            )
            CarouselMediasFilter(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                filters = module.filters,
                onclick = { filter -> onFilterClick(module, filter) },
            )
            CarouselMedias(
                modifier = Modifier,
                items = module.medias,
                onItemClick = { item -> onItemClick(module, item) },
                onItemWatchButtonClick = { item -> onItemWatchButtonClick(module, item) },
            )
        }
    }
}

@DevicePreviews
@Composable
fun CarouselMediasModulePreview() {
    AppComposeTheme {
        CarouselMediasModule(
            module = popularModule,
        )
    }
}
