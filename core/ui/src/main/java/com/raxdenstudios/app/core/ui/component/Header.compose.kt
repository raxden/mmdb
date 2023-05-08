package com.raxdenstudios.app.core.ui.component

import SeeAllButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme

@Composable
fun Header(
    modifier: Modifier = Modifier,
    title: String,
    seeAllVisible: Boolean = false,
    onSeeAllClick: () -> Unit = {},
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        DoubleArrowIcon()
        H6Text(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp, bottom = 2.dp),
            text = title,
        )
        if (seeAllVisible) {
            SeeAllButton(onClick = onSeeAllClick)
        }
    }
}

@DevicePreviews
@Composable
fun HeaderPreview() {
    AppComposeTheme {
        Header(title = "Popular")
    }
}

@DevicePreviews
@Composable
fun HeaderSeeAllPreview() {
    AppComposeTheme {
        Header(title = "Popular", seeAllVisible = true)
    }
}
