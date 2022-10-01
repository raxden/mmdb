package com.raxdenstudios.app.feature.component

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.ui.component.TopAppBarBack
import com.raxdenstudios.app.feature.MediaListContract
import com.raxdenstudios.app.feature.MediaListViewModel
import com.raxdenstudios.app.feature.component.ListPreviewData.medias
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun MediaListScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaListViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit,
    onNavigateToMedia: (id: MediaId) -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is MediaListContract.UIEvent.NavigateToMedia -> onNavigateToMedia(event.mediaId)
            is MediaListContract.UIEvent.NavigateToBack -> onNavigateToBack()
        }
        viewModel.eventConsumed(event)
    }

    MediaListScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event -> viewModel.setUserEvent(event) },
    )
}

@Composable
private fun MediaListScreen(
    modifier: Modifier = Modifier,
    uiState: MediaListContract.UIState,
    onEvent: (MediaListContract.UserEvent) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBarBack(
                title = uiState.title,
                onNavigationIconClick = { onEvent(MediaListContract.UserEvent.BackClicked) },
            )
        },
    ) { paddingValues ->
        MediaGrid(
            modifier = modifier
                .padding(paddingValues),
            isRefreshing = uiState.isLoading,
            items = uiState.items,
            onRefresh = { onEvent(MediaListContract.UserEvent.Refresh) },
            onPageIndexListener = { index -> onEvent(MediaListContract.UserEvent.LoadMore(index)) },
            onItemClick = { item -> onEvent(MediaListContract.UserEvent.MediaClicked(item)) },
            onItemWatchButtonClick = { item ->
                onEvent(MediaListContract.UserEvent.WatchButtonClicked(item))
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MediaListScreenPreview() {
    AppComposeTheme {
        MediaListScreen(
            uiState = MediaListContract.UIState(
                isLoading = false,
                title = "Media List",
                items = medias,
            )
        )
    }
}
