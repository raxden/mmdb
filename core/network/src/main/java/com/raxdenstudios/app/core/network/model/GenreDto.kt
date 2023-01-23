package com.raxdenstudios.app.core.network.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

data class GenreDto(
    @Expose val id: Int,
    @Expose val name: String,
) {

    @VisibleForTesting
    constructor() : this(
        id = 0,
        name = "",
    )
}
