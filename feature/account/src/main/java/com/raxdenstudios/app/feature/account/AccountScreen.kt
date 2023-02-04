package com.raxdenstudios.app.feature.account

import android.content.pm.ActivityInfo
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.component.LockScreenOrientation
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    viewModel: AccountViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    AccountScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
private fun AccountScreen(
    modifier: Modifier = Modifier,
    uiState: AccountContract.UIState,
) {
    LockScreenOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)

    uiState.sample

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Magenta)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "My Account Screen",
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier.align(Alignment.CenterHorizontally),
            textAlign = TextAlign.Center,
            fontSize = 20.sp
        )
    }
}

@DevicePreviews
@Composable
fun AccountScreenPreview() {
    AppComposeTheme {
        AccountScreen(
            uiState = AccountContract.UIState()
        )
    }
}
