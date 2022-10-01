package com.raxdenstudios.app.core.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.DeepOrange500
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun TopAppBarBack(
    modifier: Modifier = Modifier,
    title: String,
    onNavigationIconClick: () -> Unit = {},
) {
    TopAppBar(
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = Typography.h6
            )
        },
        backgroundColor = Color.White,
        navigationIcon = {
            IconButton(
                onClick = { onNavigationIconClick() }
            ) {
                Icon(
                    painter = painterResource(id = AppIcons.BackArrow),
                    tint = DeepOrange500,
                    contentDescription = null
                )
            }
        }
    )
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
