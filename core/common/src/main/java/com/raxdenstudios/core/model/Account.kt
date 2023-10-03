package com.raxdenstudios.core.model

sealed class Account {

    abstract val id: Long

    data class Guest(
        override val id: Long,
    ) : Account() {

        companion object {

            val default = Guest(
                id = 1L
            )

            val mock = Guest(
                id = 1L
            )
        }
    }

    data class Logged(
        override val id: Long,
        val credentials: Credentials,
    ) : Account() {

        companion object {

            fun withCredentials(credentials: Credentials) = Logged(
                id = 1L,
                credentials = credentials
            )

            val empty = Logged(
                id = 0L,
                credentials = Credentials.empty
            )

            val mock = Logged(
                id = 1L,
                credentials = Credentials.mock
            )
        }
    }
}
