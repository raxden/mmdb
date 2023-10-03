package com.raxdenstudios.core.model

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

        val mock = Credentials(
            accountId = "",
            accessToken = "",
            sessionId = "",
        )
    }
}
