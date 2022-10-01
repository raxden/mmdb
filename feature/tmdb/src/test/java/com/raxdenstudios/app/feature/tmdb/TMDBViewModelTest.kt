package com.raxdenstudios.app.feature.tmdb

import androidx.lifecycle.Observer
import com.raxdenstudios.app.core.domain.ConnectUseCase
import com.raxdenstudios.app.core.domain.RequestTokenUseCase
import com.raxdenstudios.core.model.Credentials
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Ignore
import org.junit.Test

class TMDBViewModelTest {

    private val requestTokenUseCase: RequestTokenUseCase = mockk()
    private val connectUseCase: ConnectUseCase = mockk {
        coEvery { this@mockk(aToken) } returns ResultData.Success(aTMDBCredentials)
    }
    private val stateObserver: Observer<TMDBViewModel.UIState> = mockk(relaxed = true)
    private val slot = slot<TMDBViewModel.UIState.Error>()

    private val viewModel: TMDBViewModel by lazy {
        TMDBViewModel(
            requestTokenUseCase = requestTokenUseCase,
            connectUseCase = connectUseCase,
        )
    }

    @Test
    @Ignore
    fun `Given a valid token, When viewModel is initialized, Then uiState is changed`() {
        coEvery { requestTokenUseCase() } returns ResultData.Success(aToken)
        viewModel.uiSTate.observeForever(stateObserver)

        coVerify { requestTokenUseCase() }
        verify { stateObserver.onChanged(TMDBViewModel.UIState.TokenLoaded(aToken)) }
    }

    @Test
    @Ignore
    fun `Given an illegalStateException, When viewModel is initialized, Then uiState is changed`() {
        coEvery { requestTokenUseCase() } returns ResultData.Error(IllegalStateException(""))
        viewModel.uiSTate.observeForever(stateObserver)

        coVerify { requestTokenUseCase() }
        verify { stateObserver.onChanged(capture(slot)) }
        Assert.assertTrue(slot.captured.throwable is IllegalStateException)
    }

    @Test
    @Ignore
    fun `Given a valid token, When login is called, Then uiState is changed`() {
        coEvery { requestTokenUseCase() } returns ResultData.Success(aToken)
        viewModel.uiSTate.observeForever(stateObserver)

        viewModel.login(aToken)

        coVerify { connectUseCase(aToken) }
        coVerify {
            stateObserver.onChanged(
                TMDBViewModel.UIState.Connected(
                    Credentials(
                        accountId = "aAccountId",
                        accessToken = "aAccessToken",
                        sessionId = "aSessionId",
                    )
                )
            )
        }
    }

    companion object {

        private const val aToken = "aToken"
        private const val aAccountId = "aAccountId"
        private const val aAccessToken = "aAccessToken"
        private val aTMDBCredentials = Credentials(
            accountId = aAccountId,
            accessToken = aAccessToken,
            sessionId = "aSessionId",
        )
    }
}
