package com.raxdenstudios.app.core.ui.mapper

import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

class DateModelMapperTest {

    private lateinit var mapper: DateModelMapper

    @Before
    fun setUp() {
        mapper = DateModelMapper()
    }

    @Test
    fun `given a date when transform then return a date string`() {
        // Given
        val date = LocalDate.of(2020, 1, 1)

        // When
        val result = mapper.transform(date)

        // Then
        assert(result == "January 1, 2020")
    }
}
