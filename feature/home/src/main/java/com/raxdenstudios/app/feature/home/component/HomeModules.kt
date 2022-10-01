package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.raxdenstudios.app.feature.home.component.HomePreviewData.modules
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun HomeModules(
    modifier: Modifier = Modifier,
    modules: List<HomeModuleModel> = emptyList(),
    onModuleFilterClick: (HomeModuleModel.Carousel, MediaFilterModel) -> Unit = { _, _ -> },
    onModuleSeeAllClick: (HomeModuleModel.Carousel) -> Unit = {},
    onModuleItemClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
    onModuleItemWatchButtonClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        content = {
            items(
                count = modules.size,
                key = { index -> modules[index].id },
            ) { index ->
                when (val module = modules[index]) {
                    is HomeModuleModel.Carousel.NowPlaying ->
                        CarouselFeaturedMediasModule(
                            modifier = Modifier,
                            module = module,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Popular ->
                        CarouselMediasModule(
                            modifier = Modifier,
                            module = module,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.TopRated ->
                        CarouselMediasModule(
                            modifier = Modifier,
                            module = module,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Upcoming ->
                        CarouselMediasModule(
                            modifier = Modifier,
                            module = module,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Watchlist ->
                        CarouselMediasModule(
                            modifier = Modifier,
                            module = module,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun HomeModulesPreview() {
    AppComposeTheme {
        HomeModules(
            modules = modules
        )
    }
}
