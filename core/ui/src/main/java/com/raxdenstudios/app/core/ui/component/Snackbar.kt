package com.raxdenstudios.app.core.ui.component

import androidx.compose.material.SnackbarHostState
import androidx.compose.material.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember

@Composable
fun ShowSnackbarMessage(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    message: String,
    onDismiss: () -> Unit = {},
    onActionPerformed: () -> Unit = {},
) {
    // `LaunchedEffect` will cancel and re-launch if `snackbarHostState` changes
    // More info -> https://developer.android.com/jetpack/compose/side-effects#launchedeffect
    LaunchedEffect(snackbarHostState) {
        // Show snackbar using a coroutine, when the coroutine is cancelled the
        // snackbar will automatically dismiss. This coroutine will cancel whenever
        // `state.hasError` is false, and only start when `state.hasError` is true
        // (due to the above if-check), or if `scaffoldState.snackbarHostState` changes.
        when (snackbarHostState.showSnackbar(message)) {
            SnackbarResult.Dismissed -> onDismiss()
            SnackbarResult.ActionPerformed -> onActionPerformed()
        }
    }
}
