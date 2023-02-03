package com.raxdenstudios.app.feature.detail.component

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.TopAppBarBack
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.Grey900Translucent
import com.raxdenstudios.app.feature.detail.MediaContract
import com.raxdenstudios.app.feature.detail.MediaViewModel

@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaViewModel = hiltViewModel(),
    onNavigateToBack: () -> Unit,
) {

    val uiState by viewModel.uiState.collectAsState()
    uiState.events.firstOrNull()?.let { event ->
        when (event) {
            is MediaContract.UIEvent.NavigateToBack -> onNavigateToBack()
        }
        viewModel.eventConsumed(event)
    }

    MediaScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event -> viewModel.setUserEvent(event) },
    )
}

private const val ANIMATION_DURATION = 300

@Composable
private fun MediaScreen(
    modifier: Modifier = Modifier,
    uiState: MediaContract.UIState,
    onEvent: (MediaContract.UserEvent) -> Unit = {},
) {
    var headerHeightPx by remember { mutableStateOf(0f) }
    var topAppBarHeightPx by remember { mutableStateOf(0f) }
//    val statusBarHeight = WindowInsets.statusBars.getTop(LocalDensity.current)
    val scrollState = rememberScrollState()
    val showTopAppBar by remember {
        // https://developer.android.com/jetpack/compose/side-effects?hl=es-419#derivedstateof
        derivedStateOf { // derivedStateOf allows you to derive a state value from another state value.
            scrollState.value >= (headerHeightPx - topAppBarHeightPx)
        }
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(scrollState),
        ) {
            MediaHeader(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        headerHeightPx = coordinates.size.height.toFloat()
                    },
                scrollState = scrollState,
                media = uiState.media,
            )
            MediaBody(
                modifier = Modifier
                    .fillMaxWidth(),
                media = uiState.media,
            )
        }
        BackButton(
            modifier = Modifier
                .statusBarsPadding(),
            onNavigateToBack = { onEvent(MediaContract.UserEvent.BackClicked) }
        )
        AnimatedVisibility(
            visible = showTopAppBar,
            enter = fadeIn(animationSpec = tween(ANIMATION_DURATION)),
            exit = fadeOut(animationSpec = tween(ANIMATION_DURATION))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .onGloballyPositioned { coordinates ->
                        topAppBarHeightPx = coordinates.size.height.toFloat()
                    },
            ) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.background)
                        .windowInsetsTopHeight(WindowInsets.statusBars)
                )
                TopAppBarBack(
                    modifier = Modifier,
                    title = uiState.media.title,
                    onNavigationIconClick = { onEvent(MediaContract.UserEvent.BackClicked) },
                )
            }
        }
        AddToWatchlistFloatingActionButton(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            addToWatchlist = { onEvent(MediaContract.UserEvent.AddToWatchlist(media = uiState.media)) },
            removeFromWatchlist = { onEvent(MediaContract.UserEvent.RemoveFromWatchlist(media = uiState.media)) },
            isMediaAddedToWatchlist = uiState.media.watchlist,
        )
    }
}

@Composable
private fun AddToWatchlistFloatingActionButton(
    modifier: Modifier = Modifier,
    addToWatchlist: () -> Unit,
    removeFromWatchlist: () -> Unit,
    isMediaAddedToWatchlist: Boolean
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = {
            if (isMediaAddedToWatchlist)
                removeFromWatchlist()
            else addToWatchlist()
        },
    ) {
        Icon(
            painter = painterResource(getResourceId(isMediaAddedToWatchlist)),
            contentDescription = null
        )
    }
}

@Composable
private fun getResourceId(isSelected: Boolean): Int = when (isSelected) {
    true -> AppIcons.Selected
    false -> AppIcons.Unselected
}

@Composable
private fun BackButton(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit,
) {
    IconButton(
        onClick = onNavigateToBack,
        modifier = modifier
            .padding(horizontal = 10.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Grey900Translucent,
                shape = CircleShape
            )
    ) {
        Icon(
            painter = painterResource(id = AppIcons.BackArrow),
            tint = Color.White,
            contentDescription = null
        )
    }
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaScreenPreview() {
    AppComposeTheme {
        MediaScreen(
            uiState = MediaContract.UIState(
                media = MediaModel.mock
            ),
        )
    }
}
