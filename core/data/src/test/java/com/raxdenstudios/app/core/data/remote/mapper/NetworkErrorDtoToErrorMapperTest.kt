package com.raxdenstudios.app.core.data.remote.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.network.model.NetworkErrorDto
import com.raxdenstudios.commons.NetworkError
import org.junit.Before
import org.junit.Test

@Suppress("UNCHECKED_CAST")
internal class NetworkErrorDtoToErrorMapperTest {

    private lateinit var mapper: NetworkErrorDtoToErrorMapper

    @Before
    fun setUp() {
        mapper = NetworkErrorDtoToErrorMapper()
    }

    @Test
    fun `transform should return a client error`() {
        val source = NetworkError.Client(code = 0, body = "Client error", message = "Client error")

        val result = mapper.transform(source as NetworkErrorDto)

        assertThat(result).isEqualTo(ErrorDomain.Network.Client("Client error"))
    }

    @Test
    fun `transform should return a connection error`() {
        val source = NetworkError.Network(body = "Network error", message = "Network error")

        val result = mapper.transform(source as NetworkErrorDto)

        assertThat(result).isEqualTo(ErrorDomain.Network.Connection("Network error"))
    }

    @Test
    fun `transform should return a server error`() {
        val source = NetworkError.Server(code = 0, body = "Server error", message = "Server error")

        val result = mapper.transform(source as NetworkErrorDto)

        assertThat(result).isEqualTo(ErrorDomain.Network.Server("Server error"))
    }

    @Test
    fun `transform should return a unknown error`() {
        val source = NetworkError.Unknown(code = 0, body = "Unknown error", message = "Unknown error")

        val result = mapper.transform(source as NetworkErrorDto)

        assertThat(result).isEqualTo(ErrorDomain.Network.Unknown("Unknown error"))
    }
}
