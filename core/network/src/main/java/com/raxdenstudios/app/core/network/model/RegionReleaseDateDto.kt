package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class RegionReleaseDateDto(
    @Expose val iso_3166_1: String,
    @Expose val release_dates: List<ReleaseDateDto>,
) {

    companion object {

        val mock = RegionReleaseDateDto(
            iso_3166_1 = "ES",
            release_dates = listOf(ReleaseDateDto.mock)
        )
    }
}
