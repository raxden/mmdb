package com.raxdenstudios.app.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.navigation.HomeRoutes
import com.raxdenstudios.app.core.navigation.MainRoutes
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MainViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel()
    }

    @Test
    fun `showBottomBar when route is home`() = runTest {
        val route = HomeRoutes.home.value

        viewModel.setCurrentRoute(route)

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState).isEqualTo(
                MainContract.UIState().copy(shouldShowBottomBar = true)
            )
        }
    }

    @Test
    fun `showBottomBar when route is search`() = runTest {
        val route = MainRoutes.search.value

        viewModel.setCurrentRoute(route)

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState).isEqualTo(
                MainContract.UIState().copy(shouldShowBottomBar = true)
            )
        }
    }

    @Test
    fun `showBottomBar when route is account`() = runTest {
        val route = MainRoutes.account.value

        viewModel.setCurrentRoute(route)

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState).isEqualTo(
                MainContract.UIState().copy(shouldShowBottomBar = true)
            )
        }
    }

    @Test
    fun `showBottomBar is false when route is other`() = runTest {
        val route = "other"

        viewModel.setCurrentRoute(route)

        viewModel.uiState.test {
            val uiState = awaitItem()

            assertThat(uiState).isEqualTo(
                MainContract.UIState().copy(shouldShowBottomBar = false)
            )
        }
    }
}
