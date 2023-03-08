package com.raxdenstudios.app.feature.detail

import androidx.compose.ui.test.SemanticsNodeInteractionsProvider
import androidx.compose.ui.test.onNodeWithContentDescription
import com.raxdenstudios.commons.android.test.TestPage

class MediaScreenTestPage(
    private val semanticsNodeInteractionsProvider: SemanticsNodeInteractionsProvider,
) : TestPage() {

    override fun verify(): MediaScreenTestPage {
        semanticsNodeInteractionsProvider.run {
            onNodeWithContentDescription("Media Header").assertExists()
            onNodeWithContentDescription("Media Body").assertExists()
            onNodeWithContentDescription("Back Button").assertExists()
            onNodeWithContentDescription("Add to watchlist").assertExists()
        }
        return this
    }

    fun errorDialogIsDisplayed(): MediaScreenTestPage {
        semanticsNodeInteractionsProvider.run {
            onNodeWithContentDescription("Error Dialog").assertExists()
        }
        return this
    }
}
