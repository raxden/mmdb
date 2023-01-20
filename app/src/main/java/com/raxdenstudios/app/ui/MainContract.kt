package com.raxdenstudios.app.ui

/**
 * https://developer.android.com/topic/architecture/ui-layer
 * https://developer.android.com/topic/architecture/ui-layer/events#consuming-trigger-updates
 * https://medium.com/androiddevelopers/viewmodel-one-off-event-antipatterns-16a1da869b95
 */
object MainContract {

    data class UIState(
        val shouldShowBottomBar: Boolean = true,
    )
}
