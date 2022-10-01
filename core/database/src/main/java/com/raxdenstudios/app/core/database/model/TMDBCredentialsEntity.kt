package com.raxdenstudios.app.core.database.model

data class TMDBCredentialsEntity(
    val accountId: String,
    val accessToken: String,
    val sessionId: String,
) {

    companion object {

        val default = TMDBCredentialsEntity(
            accountId = "",
            accessToken = "",
            sessionId = "",
        )
    }
}
