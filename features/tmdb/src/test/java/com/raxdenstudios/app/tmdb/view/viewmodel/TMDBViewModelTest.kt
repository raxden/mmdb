package com.raxdenstudios.app.tmdb.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.app.tmdb.di.tmdbFeatureModule
import com.raxdenstudios.app.tmdb.domain.ConnectUseCase
import com.raxdenstudios.app.tmdb.domain.RequestTokenUseCase
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import org.junit.Assert.assertTrue
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class TMDBViewModelTest : BaseTest() {

  private val requestTokenUseCase: RequestTokenUseCase = mockk {
    coEvery { execute() } returns ResultData.Success(aToken)
  }
  private val connectUseCase: ConnectUseCase = mockk {
    coEvery { execute(aToken) } returns ResultData.Success(aTMDBCredentials)
  }
  private val stateObserver: Observer<UIState> = mockk(relaxed = true)
  private val slot = slot<UIState.Error>()

  override val modules: List<Module>
    get() = listOf(
      tmdbFeatureModule,
      module {
        factory(override = true) { requestTokenUseCase }
        factory(override = true) { connectUseCase }
      }
    )

  private val viewModel: TMDBViewModel by inject()

  @Test
  fun `Given a viewModel, When viewModel is started, Then requestToken call is called and a valid token is returned`() {
    viewModel.state.observeForever(stateObserver)

    coVerify {
      stateObserver.onChanged(UIState.TokenLoaded(aToken))
      requestTokenUseCase.execute()
    }
  }

  @Test
  fun `Given a viewModel, When viewModel is started, Then requestToken call is called and a error is returned`() {
    coEvery {
      requestTokenUseCase.execute()
    } returns ResultData.Error(IllegalStateException(""))

    viewModel.state.observeForever(stateObserver)

    coVerify {
      stateObserver.onChanged(capture(slot))
      requestTokenUseCase.execute()
    }
    assertTrue(slot.captured.throwable is IllegalStateException)
  }

  @Test
  fun `Given a valid token, When requestAccessToken is called, Then AccessTokenLoaded state with accesstoken is returned`() {
    viewModel.state.observeForever(stateObserver)
    viewModel.login(aToken)

    coVerify {
      stateObserver.onChanged(
        UIState.Connected(
          Credentials(
            accountId = "aAccountId",
            accessToken = "aAccessToken",
            sessionId = "aSessionId",
          )
        )
      )
      connectUseCase.execute(aToken)
    }
  }
}

private const val aToken = "aToken"
private const val aAccountId = "aAccountId"
private const val aAccessToken = "aAccessToken"
private val aTMDBCredentials = Credentials(
  accountId = aAccountId,
  accessToken = aAccessToken,
  sessionId = "aSessionId",
)
