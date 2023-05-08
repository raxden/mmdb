package com.raxdenstudios.app.core.ui.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test
import java.util.Locale

class LanguageModelMapperTest {

    private lateinit var languageModelMapper: LanguageModelMapper

    @Before
    fun setUp() {
        languageModelMapper = LanguageModelMapper()
    }

    @Test
    fun `given a locale when transform then return the language`() {
        // Given
        val locale = Locale("es", "ES")

        // When
        val language = languageModelMapper.transform(locale)

        // Then
        assertThat(language).isEqualTo("Spanish")
    }
}
