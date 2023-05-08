package com.raxdenstudios.app.core.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val DarkColorPalette = darkColors(
    primary = Grey50,
    primaryVariant = Grey50,
    secondary = Grey50,
    secondaryVariant = Grey50,
    background = Color.Black,
    surface = Color.Black,
    error = BlazeOrangeDark,
    onPrimary = DeepOrange500,
    onSecondary = DeepOrange500,
    onBackground = Grey50,
    onSurface = Grey50,
    onError= Grey50,
)

private val LightColorPalette = lightColors(
    primary = DeepOrange500,
    primaryVariant = DeepOrange500,
    secondary = DeepOrange500,
    secondaryVariant = DeepOrange500,
    background = Color.White,
    surface = Color.White,
    error = BlazeOrangeDark,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Grey900,
    onSurface = Grey900,
    onError= Color.White,
)

@Composable
fun AppComposeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = DarkColorPalette.takeIf { darkTheme } ?: LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = {
            SystemUiController(
                color = WhiteTranslucent,
                useDarkIcons = !darkTheme
            )
            Surface(color = colors.background) {
                content()
            }
        }
    )
}

@Composable
fun SystemUiController(
    color: Color = Color.Transparent,
    useDarkIcons : Boolean = !isSystemInDarkTheme()
) {
    val systemUiController = rememberSystemUiController()

    DisposableEffect(systemUiController, useDarkIcons) {
        // Update all of the system bar colors to be transparent, and use
        // dark icons if we're in light theme
        systemUiController.setSystemBarsColor(
            color = color,
            darkIcons = useDarkIcons
        )

        systemUiController.setStatusBarColor(
            color = color,
            darkIcons = useDarkIcons
        )
        // setStatusBarColor() and setNavigationBarColor() also exist
        onDispose {}
    }
}
