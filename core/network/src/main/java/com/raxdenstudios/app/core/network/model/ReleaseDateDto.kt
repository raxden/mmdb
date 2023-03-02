package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class ReleaseDateDto(
    @Expose val certification: String,
    @Expose val iso_639_1: String,
    @Expose val note: String,
    @Expose val release_date: String,
    @Expose val type: Int,
) {

    companion object {

        val mock = ReleaseDateDto(
            certification = "PEGI 18",
            iso_639_1 = "en",
            note = "",
            release_date = "2019-12-13",
            type = 3,
        )
    }
}
