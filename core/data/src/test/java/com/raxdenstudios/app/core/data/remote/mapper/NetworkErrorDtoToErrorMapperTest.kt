package com.raxdenstudios.app.core.data.remote.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.commons.NetworkError
import org.junit.Before
import org.junit.Test

internal class NetworkErrorDtoToErrorMapperTest {

    private lateinit var mapper: NetworkErrorDtoToErrorMapper

    @Before
    fun setUp() {
        mapper = NetworkErrorDtoToErrorMapper()
    }

    @Test
    fun `transform should return a client error`() {
        val result = mapper.transform(clientError)

        assertThat(result).isEqualTo(ErrorDomain.Network.Client("Client error"))
    }

    @Test
    fun `transform should return a connection error`() {
        val result = mapper.transform(networkError)

        assertThat(result).isEqualTo(ErrorDomain.Network.Connection("Network error"))
    }

    @Test
    fun `transform should return a server error`() {
        val result = mapper.transform(serverError)

        assertThat(result).isEqualTo(ErrorDomain.Network.Server("Server error"))
    }

    @Test
    fun `transform should return a unknown error`() {
        val result = mapper.transform(unknownError)

        assertThat(result).isEqualTo(ErrorDomain.Network.Unknown("Unknown error"))
    }

    companion object {

        private val clientError = NetworkError.Client(
            code = 0,
            body = ErrorDto(status_code = 1, status_message = "Client error"),
            message = "Client error"
        )
        private val networkError = NetworkError.Network(
            body = ErrorDto(status_code = 1, status_message = "Network error"),
            message = "Network error"
        )
        private val serverError = NetworkError.Server(
            code = 0,
            body = ErrorDto(status_code = 1, status_message = "Server error"),
            message = "Server error"
        )
        private val unknownError = NetworkError.Unknown(
            code = 0,
            body = ErrorDto(status_code = 1, status_message = "Unknown error"),
            message = "Unknown error"
        )
    }
}
