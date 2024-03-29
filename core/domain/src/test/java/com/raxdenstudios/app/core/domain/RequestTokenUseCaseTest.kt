package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.AuthenticationRepository
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

internal class RequestTokenUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val authenticationRepository: AuthenticationRepository = mockk {
        coEvery { requestToken() } returns ResultData.Success("token")
    }
    private val useCase: RequestTokenUseCase by lazy {
        RequestTokenUseCase(
            authenticationRepository = authenticationRepository,
        )
    }

    @Test
    fun `Should request token`() = runTest {
        val result = useCase()

        assertThat(result).isEqualTo(ResultData.Success("token"))
    }
}
