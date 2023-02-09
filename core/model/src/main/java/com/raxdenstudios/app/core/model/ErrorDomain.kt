package com.raxdenstudios.app.core.model

sealed interface ErrorDomain {

    val message: String

    sealed interface Network : ErrorDomain {

        data class Client(
            override val message: String,
        ) : Network

        data class Server(
            override val message: String,
        ) : Network

        data class Connection(
            override val message: String,
        ) : Network

        data class Unknown(
            override val message: String,
        ) : Network
    }

    data class Unauthorized(
        override val message: String,
    ) : ErrorDomain

    data class ResourceNotFound(
        override val message: String,
    ) : ErrorDomain

    data class Unknown(
        override val message: String,
    ) : ErrorDomain
}
