package com.raxdenstudios.app.feature.search

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
import com.raxdenstudios.app.feature.search.SearchContract
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.feature.search.SearchViewModel

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    SearchScreen(
        modifier = modifier,
        uiState = uiState,
    )
}

@Composable
private fun SearchScreen(
    modifier: Modifier = Modifier,
    uiState: SearchContract.UIState,
) {
    uiState.query

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color.Cyan)
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "My Search Screen",
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
fun SearchScreenPreview() {
    AppComposeTheme {
        SearchScreen(
            uiState = SearchContract.UIState()
        )
    }
}
