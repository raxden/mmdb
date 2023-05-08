package com.raxdenstudios.app.core.ui.mapper

import org.junit.Before
import org.junit.Test

class CurrencyModelMapperTest {

    private lateinit var mapper: CurrencyModelMapper

    @Before
    fun setUp() {
        mapper = CurrencyModelMapper()
    }

    @Test
    fun `given a double value when transform then return a currency string`() {
        // Given
        val currencyModelMapper = CurrencyModelMapper()
        val value = 100.0

        // When
        val result = currencyModelMapper.transform(value)

        // Then
        assert(result == "$100.00")
    }

    @Test
    fun `given a zero double value when transform then return an empty string`() {
        // Given
        val currencyModelMapper = CurrencyModelMapper()
        val value = 0.0

        // When
        val result = currencyModelMapper.transform(value)

        // Then
        assert(result == "")
    }
}
