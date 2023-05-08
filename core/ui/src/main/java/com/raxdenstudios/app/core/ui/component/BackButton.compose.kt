package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.raxdenstudios.app.core.ui.icon.AppIcons
import com.raxdenstudios.app.core.ui.theme.Grey900Translucent

@Composable
fun BackButton(
    modifier: Modifier = Modifier,
    onNavigateToBack: () -> Unit,
) {
    IconButton(
        onClick = onNavigateToBack,
        modifier = modifier
            .semantics { contentDescription = "Back Button" }
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
