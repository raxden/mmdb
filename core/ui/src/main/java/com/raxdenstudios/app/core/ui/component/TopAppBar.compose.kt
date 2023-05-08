package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AppBarDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.DeepOrange500
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun TopAppBarBack(
    modifier: Modifier = Modifier,
    title: String,
    elevation: Dp = AppBarDefaults.TopAppBarElevation,
    backgroundColor: Color = MaterialTheme.colors.surface,
    navigationIconTintColor: Color = MaterialTheme.colors.secondary,
    onNavigationIconClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier
            .fillMaxWidth(),
        title = {
            NavigationTitle(title)
        },
        backgroundColor = backgroundColor,
        navigationIcon = {
            NavigationBackIcon(
                tintColor = navigationIconTintColor,
                onNavigationIconClick = onNavigationIconClick,
            )
        },
        elevation = elevation,
    )
}

@Composable
private fun NavigationTitle(title: String) {
    Text(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 16.dp),
        style = Typography.h6,
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun NavigationBackIcon(
    tintColor: Color = DeepOrange500,
    onNavigationIconClick: () -> Unit = {},
) {
    IconButton(
        onClick = { onNavigationIconClick() }
    ) {
        Icon(
            painter = painterResource(id = AppIcons.BackArrow),
            tint = tintColor,
            contentDescription = null
        )
    }
}

@DevicePreviews
@Composable
fun TopAppBarBackPreview() {
    AppComposeTheme {
        TopAppBarBack(
            title = "Title",
        )
    }
}
