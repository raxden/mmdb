package com.raxdenstudios.core.model

import androidx.annotation.VisibleForTesting

data class Credentials(
    val accountId: String,
    val accessToken: String,
    val sessionId: String,
) {

    companion object {

        val empty = Credentials(
            accountId = "",
            accessToken = "",
            sessionId = "",
        )

        @VisibleForTesting
        val mock = Credentials(
            accountId = "",
            accessToken = "",
            sessionId = "",
        )
    }
}
