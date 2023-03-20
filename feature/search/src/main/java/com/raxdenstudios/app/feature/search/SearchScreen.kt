package com.raxdenstudios.app.feature.search

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.component.MediaGrid
import com.raxdenstudios.app.core.ui.component.StatusBarSpacer
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.search.component.SearchTopBar
import com.raxdenstudios.app.feature.search.model.SearchBarModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToMedia: (id: MediaId, type: MediaType) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is SearchContract.UIEvent.NavigateToMedia -> {
                onNavigateToMedia(event.mediaId, event.mediaType)
                viewModel.eventConsumed(event)
            }
        }
    }

    SearchScreen(
        modifier = modifier,
        uiState = uiState,
        onUIEvent = { event -> viewModel.setUserEvent(event) },
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchContract.UIState,
    onUIEvent: (SearchContract.UserEvent) -> Unit = { },
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    Column(
        modifier = modifier,
    ) {
        StatusBarSpacer()
        SearchTopBar(
            modifier = Modifier.fillMaxWidth()
                .zIndex(1f),
            model = uiState.searchBarModel,
            onSearchClick = { query -> onUIEvent(SearchContract.UserEvent.SearchClicked(query)) },
            onSearchTextChanged = { query -> onUIEvent(SearchContract.UserEvent.SearchBarQueryChanged(query)) },
            onSearchClearClick = { onUIEvent(SearchContract.UserEvent.ClearSearchBarClicked) },
        )
        MediaGrid(
            modifier = Modifier.fillMaxSize()
                .zIndex(0f),
            items = uiState.results,
            onItemClick = { item -> onUIEvent(SearchContract.UserEvent.MediaClicked(item)) },
            onItemWatchButtonClick = { item -> onUIEvent(SearchContract.UserEvent.MediaWatchButtonClicked(item)) },
        )
    }
}

@SuppressLint("VisibleForTests")
@SuppressWarnings("MagicNumber")
@DevicePreviews
@Composable
fun SearchScreenPreview() {
    AppComposeTheme {
        SearchScreen(
            uiState = SearchContract.UIState(
                searchBarModel = SearchBarModel.WithResults("query"),
                results = listOf(
                    MediaModel.mock
                ),
            )
        )
    }
}
