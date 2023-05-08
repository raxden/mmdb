package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.Header
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Shapes
import com.raxdenstudios.app.feature.home.component.PreviewData.nowPlayingModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel

@Composable
fun CarouselFeaturedMediasModule(
    modifier: Modifier = Modifier,
    module: HomeModuleModel.Carousel,
    isFirstModule: Boolean = false,
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
            Header(
                modifier = if (isFirstModule) Modifier.statusBarsPadding() else Modifier,
                title = module.label,
                seeAllVisible = true,
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

@DevicePreviews
@Composable
fun CarouselFeaturedMediasModulePreview() {
    AppComposeTheme {
        CarouselFeaturedMediasModule(
            module = nowPlayingModule,
        )
    }
}
