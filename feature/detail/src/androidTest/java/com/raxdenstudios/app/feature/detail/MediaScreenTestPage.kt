package com.raxdenstudios.app.feature.detail

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.raxdenstudios.app.test.HiltTestActivity
import com.raxdenstudios.commons.android.test.TestPage

class MediaScreenTestPage(
    private val composeTestRule: AndroidComposeTestRule<ActivityScenarioRule<HiltTestActivity>, HiltTestActivity>,
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
            onNodeWithContentDescription("Error Dialog").assertExists()
        }
        return this
    }

    fun clickBackButton(): MediaScreenTestPage {
        composeTestRule.run {
            onNodeWithContentDescription("Back Button").performClick()
        }
        return this
    }

    fun clickYoutubeVideo(): MediaScreenTestPage {
        composeTestRule.run {
            onNodeWithContentDescription("Back Button").performClick()
        }
        return this
    }
}
