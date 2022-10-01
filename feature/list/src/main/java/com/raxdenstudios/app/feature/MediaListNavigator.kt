package com.raxdenstudios.app.feature

import com.raxdenstudios.app.core.model.MediaId

interface MediaListNavigator {

    fun login(onSuccess: () -> Unit = {})

    fun back()

    fun media(mediaId: MediaId)
}
