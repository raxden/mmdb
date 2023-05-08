package com.raxdenstudios.app.core.ui.extension

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale

@Stable
fun Modifier.mirror(): Modifier = this.scale(scaleX = -1f, scaleY = 1f)
