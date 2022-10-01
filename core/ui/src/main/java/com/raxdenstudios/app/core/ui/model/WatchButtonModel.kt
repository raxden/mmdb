package com.raxdenstudios.app.core.ui.model

sealed class WatchButtonModel {

    object Selected : WatchButtonModel()
    object Unselected : WatchButtonModel()
}
