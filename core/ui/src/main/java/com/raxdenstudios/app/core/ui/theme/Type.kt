package com.raxdenstudios.app.core.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    h1 = Typography().h1.copy(
        color = Color.Black,
    ),
    h2 = Typography().h2.copy(
        color = Color.Black,
    ),
    h3 = Typography().h3.copy(
        color = Color.Black,
    ),
    h4 = Typography().h4.copy(
        color = Color.Black,
    ),
    h5 = Typography().h5.copy(
        color = Color.Black,
    ),
    h6 = Typography().h6.copy(
        color = Color.Black,
    ),
    subtitle1 = Typography().subtitle1.copy(
        color = Color.Black,
    ),
    subtitle2 = Typography().subtitle2.copy(
        color = Color.Black,
    ),
    body1 = Typography().body1.copy(
        color = Color.Black,
    ),
    body2 = Typography().body2.copy(
        color = Color.Black,
    ),
    button = Typography().button.copy(
        color = Color.Black,
    ),
    caption = Typography().caption.copy(
        color = Color.Black,
    ),
    overline = Typography().overline.copy(
        color = Color.Black,
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
