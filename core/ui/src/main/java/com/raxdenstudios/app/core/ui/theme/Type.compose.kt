package com.raxdenstudios.app.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = Typography().h1.copy(
    ),
    h2 = Typography().h2.copy(
    ),
    h3 = Typography().h3.copy(
    ),
    h4 = Typography().h4.copy(
    ),
    h5 = Typography().h5.copy(
    ),
    h6 = Typography().h6.copy(
    ),
    subtitle1 = Typography().subtitle1.copy(
    ),
    subtitle2 = Typography().subtitle2.copy(
    ),
    body1 = Typography().body1.copy(
    ),
    body2 = Typography().body2.copy(
    ),
    button = Typography().button.copy(
    ),
    caption = Typography().caption.copy(
    ),
    overline = Typography().overline.copy(
    ),
    /* Other default text styles to override
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)

val Typography.sample: TextStyle
    @Composable
    get() = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    )
