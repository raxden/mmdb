package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.DevicePreviews
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.AppComposeTheme
import com.raxdenstudios.app.core.ui.theme.BlackTranslucent
import com.raxdenstudios.app.core.ui.theme.DeepOrange500

private const val CONTENT_DESCRIPTION = "WatchButton"

@Composable
fun WatchButton(
    modifier: Modifier = Modifier,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(bottomEnd = 8.dp),
    ) {
        Image(
            painter = getPainterResource(isSelected),
            colorFilter = ColorFilter.tint(Color.White),
            contentDescription = CONTENT_DESCRIPTION,
            modifier = modifier
                .width(36.dp)
                .height(46.dp)
                .background(color = getBackgroundColor(isSelected))
                .clickable { onClick() }
        )
    }
}

@Composable
private fun getPainterResource(isSelected: Boolean): Painter = when (isSelected) {
    true -> painterResource(id = AppIcons.Selected)
    false -> painterResource(id = AppIcons.Unselected)
}

@Composable
private fun getBackgroundColor(isSelected: Boolean): Color {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    return if (isPressed) {
        DeepOrange500
    } else when (isSelected) {
        true -> DeepOrange500
        false -> BlackTranslucent
    }
}

@DevicePreviews
@Composable
fun WatchButtonPreview() {
    AppComposeTheme {
        WatchButton(isSelected = true, onClick = {})
    }
}
