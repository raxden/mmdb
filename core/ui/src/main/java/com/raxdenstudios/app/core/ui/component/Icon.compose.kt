package com.raxdenstudios.app.core.ui.component

import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import com.raxdenstudios.app.core.ui.icon.AppIcons

@Composable
fun DoubleArrowIcon(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = AppIcons.DoubleArrow),
    tint: Color = MaterialTheme.colors.secondary,
    contentDescription: String? = null,
) {
    Icon(
        modifier = modifier,
        painter = painter,
        tint = tint,
        contentDescription = contentDescription,
    )
}

@Composable
fun RightArrowIcon(
    modifier: Modifier = Modifier,
    painter: Painter = painterResource(id = AppIcons.RightArrow),
    tint: Color = MaterialTheme.colors.secondary,
    contentDescription: String? = null,
) {
    Icon(
        modifier = modifier,
        painter = painter,
        tint = tint,
        contentDescription = contentDescription,
    )
}

@Composable
fun FilterIcon(
    modifier: Modifier = Modifier,
    imageVector : ImageVector = AppIcons.Filter,
    tint: Color = MaterialTheme.colors.secondary,
    contentDescription: String? = null,
) {
    Icon(
        modifier = modifier,
        imageVector = imageVector,
        tint = tint,
        contentDescription = contentDescription,
    )
}
