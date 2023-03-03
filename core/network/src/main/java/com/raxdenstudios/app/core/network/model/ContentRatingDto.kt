package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class ContentRatingDto(
    @Expose val iso_3166_1: String,
    @Expose val rating: String,
) {

    companion object {

        val mock = ContentRatingDto(
            iso_3166_1 = "ES",
            rating = "PG-13"
        )
    }
}
