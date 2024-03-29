package com.raxdenstudios.app.feature.home.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.model.MediaFilterModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.home.component.PreviewData.modules
import com.raxdenstudios.app.feature.home.model.HomeModuleModel

@Composable
fun HomeModules(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    modules: List<HomeModuleModel> = emptyList(),
    onModuleFilterClick: (HomeModuleModel.Carousel, MediaFilterModel) -> Unit = { _, _ -> },
    onModuleSeeAllClick: (HomeModuleModel.Carousel) -> Unit = {},
    onModuleItemClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
    onModuleItemWatchButtonClick: (HomeModuleModel.Carousel, MediaModel) -> Unit = { _, _ -> },
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
        content = {
            items(
                count = modules.size,
                key = { index -> modules[index].id },
            ) { index ->
                val isFirstModule = index == 0
                when (val module = modules[index]) {
                    is HomeModuleModel.Carousel.NowPlaying ->
                        CarouselFeaturedMediasModule(
                            modifier = Modifier
                                .semantics { contentDescription = "CarouselFeaturedModule" },
                            module = module,
                            isFirstModule = isFirstModule,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Popular ->
                        CarouselMediasModule(
                            modifier = Modifier
                                .semantics { contentDescription = "CarouselPopularModule" },
                            module = module,
                            isFirstModule = isFirstModule,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.TopRated ->
                        CarouselMediasModule(
                            modifier = Modifier
                                .semantics { contentDescription = "CarouselTopRatedModule" },
                            module = module,
                            isFirstModule = isFirstModule,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Upcoming ->
                        CarouselMediasModule(
                            modifier = Modifier
                                .semantics { contentDescription = "CarouselUpcomingModule" },
                            module = module,
                            isFirstModule = isFirstModule,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.Carousel.Watchlist ->
                        CarouselMediasModule(
                            modifier = Modifier
                                .semantics { contentDescription = "CarouselWatchlistModule" },
                            module = module,
                            isFirstModule = isFirstModule,
                            onFilterClick = onModuleFilterClick,
                            onSeeAllClick = onModuleSeeAllClick,
                            onItemClick = onModuleItemClick,
                            onItemWatchButtonClick = onModuleItemWatchButtonClick,
                        )
                    is HomeModuleModel.OtherModule -> Unit
                }
            }
        }
    )
}

@DevicePreviews
@Composable
fun HomeModulesPreview() {
    AppComposeTheme {
        HomeModules(
            modules = modules
        )
    }
}
