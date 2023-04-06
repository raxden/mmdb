package com.raxdenstudios.app.feature.detail

import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onAllNodesWithContentDescription
import androidx.compose.ui.test.onNodeWithContentDescription
import com.raxdenstudios.commons.android.test.TestPage

class MediaScreenTestPage(
    private val composeTestRule: ComposeContentTestRule,
) : TestPage() {

    override fun verify(): MediaScreenTestPage {
        composeTestRule.run {
            onNodeWithContentDescription("Media Header").assertExists()
            onNodeWithContentDescription("Media Body").assertExists()
            onNodeWithContentDescription("Back Button").assertExists()
            onNodeWithContentDescription("Add to watchlist").assertExists()
        }
        return this
    }

    fun errorDialogIsDisplayed(): MediaScreenTestPage {
        composeTestRule.run {
            waitUntil {
                onAllNodesWithContentDescription("Error Dialog")
                    .fetchSemanticsNodes().size == 1
            }
            onNodeWithContentDescription("Error Dialog").assertExists()
        }
        return this
    }
}
