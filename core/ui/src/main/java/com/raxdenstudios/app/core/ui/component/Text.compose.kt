package com.raxdenstudios.app.core.ui.component

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.raxdenstudios.app.core.ui.theme.Typography

@Composable
fun H6Text(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
) {
    Text(
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        style = Typography.h6.copy(
            fontWeight = FontWeight.Bold,
        ),
        text = text,
    )
}

@Composable
fun Body2Text(
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
    maxLines: Int = Int.MAX_VALUE,
    text: String,
) {
    Text(
        modifier = modifier,
        textAlign = textAlign,
        maxLines = maxLines,
        style = Typography.body2.copy(
            fontWeight = FontWeight.Medium,
        ),
        text = text,
    )
}
