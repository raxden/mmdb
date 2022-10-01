package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.feature.home.component.HomePreviewData.nowPlayingModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Shapes

@Composable
fun CarouselFeaturedMediasModule(
    modifier: Modifier = Modifier,
    module: HomeModuleModel.Carousel,
    onSeeAllClick: (HomeModuleModel.Carousel) -> Unit = {},
    onItemClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
    onItemWatchButtonClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
) {
    Surface(
        modifier = modifier
            .padding(bottom = 6.dp)
            .fillMaxWidth(),
        shape = Shapes.large,
        elevation = 6.dp,
    ) {
        Column(
            modifier = Modifier
        ) {
            CarouselMediasHeader(
                modifier = Modifier
                    .padding(all = 8.dp),
                title = module.label,
                onSeeAllClick = { onSeeAllClick(module) },
            )
            CarouselFeaturedMedias(
                modifier = Modifier,
                items = module.medias,
                onItemClick = { item -> onItemClick(module, item) },
                onItemWatchButtonClick = { item -> onItemWatchButtonClick(module, item) },
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CarouselFeaturedMediasModulePreview() {
    AppComposeTheme {
        CarouselFeaturedMediasModule(
            module = nowPlayingModule,
        )
    }
}
