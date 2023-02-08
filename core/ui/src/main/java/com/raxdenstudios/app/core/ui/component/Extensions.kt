package com.raxdenstudios.app.core.ui.component

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.Modifier

private const val ASPECT_RATIO_16_9 = 16f / 9f
private const val ASPECT_RATIO_2_3 = 2f / 3f

fun Modifier.aspectRatio169() = aspectRatio(ASPECT_RATIO_16_9)

fun Modifier.aspectRatio23() = aspectRatio(ASPECT_RATIO_2_3)
