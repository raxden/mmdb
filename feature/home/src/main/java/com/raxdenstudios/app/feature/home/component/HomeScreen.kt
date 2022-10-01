package com.raxdenstudios.app.feature.home.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.feature.home.HomeContract
import com.raxdenstudios.app.feature.home.HomeViewModel
import com.raxdenstudios.app.feature.home.component.HomePreviewData.modules
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToMediaList: (mediaType: String, mediaCategory: String) -> Unit = { _, _ -> },
) {

    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is HomeContract.UIEvent.NavigateToMediaList ->
                onNavigateToMediaList(event.mediaType.toString(), event.mediaCategory.toString())
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

@Preview(showBackground = true)
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
