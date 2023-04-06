package com.raxdenstudios.app.core.model

import androidx.annotation.VisibleForTesting

data class Vote(
    val average: Float,
    val count: Int,
) {

    companion object {

        @VisibleForTesting
        val mock = Vote(
            average = 0.0f,
            count = 0,
        )
    }
}
