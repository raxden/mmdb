package com.raxdenstudios.app.core.ui.model

import androidx.annotation.VisibleForTesting

data class ErrorModel(
    val title: String,
    val message: String,
) {

    companion object {

        @VisibleForTesting
        val mock = ErrorModel(
            title = "",
            message = "",
        )
    }
}
