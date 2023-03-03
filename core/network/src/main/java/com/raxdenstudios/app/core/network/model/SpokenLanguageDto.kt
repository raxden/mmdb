package com.raxdenstudios.app.core.network.model

import androidx.annotation.VisibleForTesting
import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming", "VariableNaming")
data class SpokenLanguageDto(
    @Expose val english_name: String,
    @Expose val iso_639_1: String,
    @Expose val name: String,
) {

    companion object {

        @VisibleForTesting
        val mock = SpokenLanguageDto(
            english_name = "English",
            iso_639_1 = "en",
            name = "English",
        )
    }
}
