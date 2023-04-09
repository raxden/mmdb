package com.raxdenstudios.app.feature.home

import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.ScaffoldState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.ErrorDialog
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is HomeContract.UIEvent.NavigateToMedias -> onNavigateToMedias(event.mediaType, event.mediaCategory)
                is HomeContract.UIEvent.NavigateToMedia -> onNavigateToMedia(event.mediaId, event.mediaType)
                is HomeContract.UIEvent.ShowMessage -> scaffoldState.snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    HomeScreen(
        modifier = modifier,
        contentPadding = WindowInsets.statusBars
            .only(WindowInsetsSides.Horizontal + WindowInsetsSides.Top).asPaddingValues(),
        uiState = uiState,
        onEvent = { event -> viewModel.setUserEvent(event) },
    )
}

@Composable
private fun HomeScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
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
        contentPadding = contentPadding,
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
