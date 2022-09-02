package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.commons.ext.getOrThrow
import javax.inject.Inject

internal class MediaListParamsFactory @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
) {

    fun create(): MediaListParams =
        savedStateHandle.getOrThrow("params", "Invalid arguments")
}
