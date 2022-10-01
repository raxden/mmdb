package com.raxdenstudios.app.core.ui.model

import com.raxdenstudios.app.core.model.MediaType

data class MediaFilterModel(
    val id: MediaType,
    val label: String,
    val isSelected: Boolean,
)
