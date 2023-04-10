package com.raxdenstudios.app.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.ui.model.BottomBarItemModel
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
internal class BottomBarViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var viewModel: BottomBarViewModel

    @Before
    fun setUp() {
        viewModel = BottomBarViewModel()
    }

    @Test
    fun `navigate to account`() = runTest {
        val event = BottomBarContract.UserEvent.ItemSelected(BottomBarItemModel.Account())

        viewModel.setUserEvent(event)

        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result).isEqualTo(
                BottomBarContract.UIState(
                    items = listOf(
                        BottomBarItemModel.Home(),
                        BottomBarItemModel.Search(),
                        BottomBarItemModel.Account(isSelected = true)
                    ),
                )
            )
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertThat(event).isEqualTo(BottomBarContract.UIEvent.NavigateToAccount)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `navigate to home`() = runTest {
        val event = BottomBarContract.UserEvent.ItemSelected(BottomBarItemModel.Home())

        viewModel.setUserEvent(event)

        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result).isEqualTo(
                BottomBarContract.UIState(
                    items = listOf(
                        BottomBarItemModel.Home(isSelected = true),
                        BottomBarItemModel.Search(),
                        BottomBarItemModel.Account()
                    ),
                )
            )
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertThat(event).isEqualTo(BottomBarContract.UIEvent.NavigateToHome)
            }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `navigate to search`() = runTest {
        val event = BottomBarContract.UserEvent.ItemSelected(BottomBarItemModel.Search())

        viewModel.setUserEvent(event)

        viewModel.uiState.test {
            val result = awaitItem()
            assertThat(result).isEqualTo(
                BottomBarContract.UIState(
                    items = listOf(
                        BottomBarItemModel.Home(),
                        BottomBarItemModel.Search(isSelected = true),
                        BottomBarItemModel.Account()
                    ),
                )
            )
            viewModel.uiEvent.test {
                val event = awaitItem()
                assertThat(event).isEqualTo(BottomBarContract.UIEvent.NavigateToSearch)
            }
            cancelAndConsumeRemainingEvents()
        }
    }
}
