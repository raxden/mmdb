package com.raxdenstudios.app.core.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.commons.android.provider.StringProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class ErrorModelMapperTest {

    private val stringProvider: StringProvider = mockk {
        every { getString(R.string.error_information) } returns "error_information"
        every { getString(R.string.error_unespected_error_message) } returns "error_unespected_error_message"
        every { getString(R.string.error_lost_connection) } returns "error_lost_connection"
    }
    private lateinit var errorModelMapper: ErrorModelMapper

    @Before
    fun setUp() {
        errorModelMapper = ErrorModelMapper(
            stringProvider = stringProvider,
        )
    }

    @Test
    fun `Given a throwable when transform then return an error model`() {
        // Given
        val throwable = Throwable()

        // When
        val errorModel = errorModelMapper.transform(throwable)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_unespected_error_message",
            )
        )
    }

    @Test
    fun `Given a network client error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Network.Client("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_message",
            )
        )
    }

    @Test
    fun `Given a network connection error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Network.Connection("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_lost_connection",
            )
        )
    }

    @Test
    fun `Given a network server error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Network.Server("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_message",
            )
        )
    }

    @Test
    fun `Given a unknown server error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Network.Unknown("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_unespected_error_message",
            )
        )
    }

    @Test
    fun `Given a resource not found error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.ResourceNotFound("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_unespected_error_message",
            )
        )
    }

    @Test
    fun `Given an unauthorized error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Unauthorized("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_unespected_error_message",
            )
        )
    }

    @Test
    fun `Given an unknown error with a message when transform then return an error model`() {
        // Given
        val clientError = ErrorDomain.Unknown("error_message")

        // When
        val errorModel = errorModelMapper.transform(clientError)

        // Then
        assertThat(errorModel).isEqualTo(
            ErrorModel(
                title = "error_information",
                message = "error_unespected_error_message",
            )
        )
    }
}
