package com.raxdenstudios.app.media.view.model

sealed class WatchButtonModel {

    object Selected : WatchButtonModel()
    object Unselected : WatchButtonModel()
}
