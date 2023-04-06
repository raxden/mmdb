package com.raxdenstudios.app.core.ui.mapper

import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class RatingModelMapperTest {

    private lateinit var ratingModelMapper: RatingModelMapper

    @Before
    fun setUp() {
        ratingModelMapper = RatingModelMapper()
    }

    @Test
    fun `given a rating when transform then return a formatted rating`() {
        val rating = 8.123456f

        val result = ratingModelMapper.transform(rating)

        assertThat(result)
            .isEqualTo("8.12")
    }
}
