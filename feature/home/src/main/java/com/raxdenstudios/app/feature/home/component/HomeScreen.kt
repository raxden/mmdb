package com.raxdenstudios.app.feature.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.home.HomeContract
import com.raxdenstudios.app.feature.home.HomeViewModel
import com.raxdenstudios.app.feature.home.component.HomePreviewData.modules

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMedia: (id: MediaId, type: MediaType) -> Unit,
    onNavigateToMedias: (mediaType: MediaType, mediaCategory: MediaCategory) -> Unit = { _, _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is HomeContract.UIEvent.NavigateToMedias ->
                onNavigateToMedias(event.mediaType, event.mediaCategory)
            is HomeContract.UIEvent.NavigateToMedia ->
                onNavigateToMedia(event.mediaId, event.mediaType)
        }
        viewModel.eventConsumed(event)
    }

    HomeScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event -> viewModel.setUserEvent(event) },
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: HomeContract.UIState,
    onEvent: (HomeContract.UserEvent) -> Unit = {},
) {
    HomeModules(
        modifier = modifier,
        modules = uiState.modules,
        onModuleFilterClick = { module, filter ->
            onEvent(HomeContract.UserEvent.MediaFilterClicked(module, filter))
        },
        onModuleSeeAllClick = { module ->
            onEvent(HomeContract.UserEvent.SeeAllButtonClicked(module))
        },
        onModuleItemClick = { _, item ->
            onEvent(HomeContract.UserEvent.MediaSelected(item))
        },
        onModuleItemWatchButtonClick = { _, item ->
            onEvent(HomeContract.UserEvent.WatchButtonClicked(item))
        },
    )
}

@DevicePreviews
@Composable
fun HomeScreenPreview() {
    AppComposeTheme {
        HomeScreen(
            uiState = HomeContract.UIState(
                modules = modules
            )
        )
    }
}
