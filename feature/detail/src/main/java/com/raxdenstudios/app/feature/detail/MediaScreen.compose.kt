package com.raxdenstudios.app.feature.detail

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.BackButton
import com.raxdenstudios.app.core.ui.component.ErrorDialog
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.component.TopAppBarBack
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.detail.component.MediaBody
import com.raxdenstudios.app.feature.detail.component.MediaHeader
import com.raxdenstudios.app.feature.detail.model.RelatedMediasModel
import com.raxdenstudios.app.feature.detail.model.VideoModel

@SuppressWarnings("LongParameterList")
@Composable
fun MediaScreen(
    modifier: Modifier = Modifier,
    viewModel: MediaViewModel = hiltViewModel(),
    onNavigateToYoutubeVideo: (String) -> Unit = {},
    onNavigateToBack: () -> Unit = {},
    onNavigateToMedia: (id: MediaId, type: MediaType) -> Unit = { _, _ -> },
    onNavigateToMedias: (mediaType: MediaType, mediaCategory: MediaCategory) -> Unit = { _, _ -> },
    onNavigateToRelatedMedias: (id: MediaId, mediaType: MediaType) -> Unit = { _, _ -> },
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is MediaContract.UIEvent.NavigateToBack -> onNavigateToBack()
                is MediaContract.UIEvent.PlayYoutubeVideo -> onNavigateToYoutubeVideo(event.url)
                is MediaContract.UIEvent.NavigateToMedia -> onNavigateToMedia(event.mediaId, event.mediaType)
                is MediaContract.UIEvent.NavigateToMedias -> onNavigateToMedias(event.mediaType, event.mediaCategory)
                is MediaContract.UIEvent.NavigateToRelatedMedias ->
                    onNavigateToRelatedMedias(event.mediaId, event.mediaType)
            }
        }
    }

    MediaScreen(
        modifier = modifier,
        uiState = uiState,
        onEvent = { event -> viewModel.setUserEvent(event) },
    )
}

private const val ANIMATION_DURATION = 300

@SuppressWarnings("LongMethod")
@Composable
private fun MediaScreen(
    modifier: Modifier = Modifier,
    uiState: MediaContract.UIState,
    onEvent: (MediaContract.UserEvent) -> Unit = {},
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

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
    if (uiState.error != null) {
        ErrorDialog(
            model = uiState.error,
            onDismiss = { onEvent(MediaContract.UserEvent.ErrorDismissed) }
        )
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
                videos = uiState.videos,
                onVideoClick = { video -> onEvent(MediaContract.UserEvent.VideoClick(video)) },
                relatedMedias = uiState.relatedMedias,
                onRelatedMediaClick = { media -> onEvent(MediaContract.UserEvent.RelatedMediaClick(media)) },
                onRelatedMediaWatchButtonClick = { media ->
                    onEvent(MediaContract.UserEvent.RelatedWatchlistClick(media))
                },
                onRelatedSeeAllClick = { onEvent(MediaContract.UserEvent.RelatedSeeAllButtonClicked) },
            )
            Spacer(modifier = Modifier.height(40.dp))
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
        WatchlistFloatingActionButton(
            modifier = Modifier
                .systemBarsPadding()
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            onClick = { onEvent(MediaContract.UserEvent.WatchlistClick(media = uiState.media)) },
            isWatchlist = uiState.media.watchlist,
        )
    }
}

@Composable
private fun WatchlistFloatingActionButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    isWatchlist: Boolean
) {
    FloatingActionButton(
        modifier = modifier
            .semantics { contentDescription = "Add to watchlist" },
        onClick = onClick,
    ) {
        Icon(
            painter = painterResource(getResourceId(isWatchlist)),
            contentDescription = null
        )
    }
}

@Composable
private fun getResourceId(isSelected: Boolean): Int = when (isSelected) {
    true -> AppIcons.Selected
    false -> AppIcons.Unselected
}

@SuppressLint("VisibleForTests")
@DevicePreviews
@Composable
fun MediaScreenPreview() {
    AppComposeTheme {
        MediaScreen(
            uiState = MediaContract.UIState(
                media = MediaModel.mock,
                videos = listOf(
                    VideoModel.mock,
                    VideoModel.mock,
                ),
                relatedMedias = RelatedMediasModel.mock,
            ),
        )
    }
}
