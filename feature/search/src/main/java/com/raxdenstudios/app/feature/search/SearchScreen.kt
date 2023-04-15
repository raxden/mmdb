package com.raxdenstudios.app.feature.search

import RecentSearches
import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.systemBars
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.component.MediaGrid
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.search.component.SearchTopBar
import com.raxdenstudios.app.feature.search.model.SearchBarModel
import com.raxdenstudios.commons.ext.toDp

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
    onNavigateToMedia: (id: MediaId, type: MediaType) -> Unit = { _, _ -> },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is SearchContract.UIEvent.NavigateToMedia ->
                    onNavigateToMedia(event.mediaId, event.mediaType)
            }
        }
    }

    SearchScreen(
        modifier = modifier,
        uiState = uiState,
        onUIEvent = { event -> viewModel.setUserEvent(event) },
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchContract.UIState,
    onUIEvent: (SearchContract.UserEvent) -> Unit = { },
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    val focusManager = LocalFocusManager.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .clickable(
                onClick = { focusManager.clearFocus() },
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
            )
    ) {
        Column {
            SearchTopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .zIndex(1f),
                contentPadding = PaddingValues(
                    start = 4.dp,
                    end = 4.dp,
                    top = WindowInsets.systemBars.getTop(LocalDensity.current).toDp().dp,
                    bottom = 4.dp
                ),
                model = uiState.searchBarModel,
                focusManager = focusManager,
                onSearchClick = { query -> onUIEvent(SearchContract.UserEvent.SearchClicked(query)) },
                onSearchTextChanged = { query -> onUIEvent(SearchContract.UserEvent.SearchBarQueryChanged(query)) },
                onSearchClearClick = { onUIEvent(SearchContract.UserEvent.ClearSearchBarClicked) },
            )
            if (uiState.recentSearches.isNotEmpty()) {
                RecentSearches(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f),
                    searches = uiState.recentSearches,
                    onRecentSearchClicked = { query -> onUIEvent(SearchContract.UserEvent.RecentSearchClicked(query)) }
                )
            }
            if (uiState.results.isNotEmpty()) {
                MediaGrid(
                    modifier = Modifier
                        .fillMaxSize()
                        .zIndex(0f),
                    items = uiState.results,
                    onItemClick = { item -> onUIEvent(SearchContract.UserEvent.MediaClicked(item)) },
                    onItemWatchClick = { item -> onUIEvent(SearchContract.UserEvent.MediaWatchButtonClicked(item)) },
                )
            }
        }
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
