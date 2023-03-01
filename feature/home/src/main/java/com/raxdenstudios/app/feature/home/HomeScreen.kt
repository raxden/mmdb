package com.raxdenstudios.app.feature.home

import android.content.pm.ActivityInfo
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.ErrorDialog
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.component.ShowSnackbarMessage
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.home.HomeContract
import com.raxdenstudios.app.feature.home.HomeViewModel
import com.raxdenstudios.app.feature.home.component.HomeModules
import com.raxdenstudios.app.feature.home.component.HomePreviewData.modules

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    scaffoldState: ScaffoldState = rememberScaffoldState(),
    onNavigateToMedia: (id: MediaId, type: MediaType) -> Unit = { _, _ -> },
    onNavigateToMedias: (mediaType: MediaType, mediaCategory: MediaCategory) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is HomeContract.UIEvent.NavigateToMedias -> {
                onNavigateToMedias(event.mediaType, event.mediaCategory)
                viewModel.eventConsumed(event)
            }
            is HomeContract.UIEvent.NavigateToMedia -> {
                onNavigateToMedia(event.mediaId, event.mediaType)
                viewModel.eventConsumed(event)
            }
            is HomeContract.UIEvent.ShowMessage ->
                ShowSnackbarMessage(
                    snackbarHostState = scaffoldState.snackbarHostState,
                    message = event.message,
                    onDismiss = { viewModel.eventConsumed(event) }
                )
        }
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
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    if (uiState.error != null) {
        ErrorDialog(
            model = uiState.error,
            onDismiss = { onEvent(HomeContract.UserEvent.ErrorDismissed) }
        )
    }
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
                modules = modules,
            )
        )
    }
}