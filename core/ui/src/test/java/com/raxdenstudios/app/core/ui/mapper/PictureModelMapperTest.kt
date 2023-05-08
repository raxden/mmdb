package com.raxdenstudios.app.core.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.Picture
import org.junit.Before
import org.junit.Test

class PictureModelMapperTest {

    private lateinit var pictureModelMapper: PictureModelMapper

    @Before
    fun setUp() {
        pictureModelMapper = PictureModelMapper()
    }

    @Test
    fun `given a picture when transform then return the picture model`() {
        // Given
        val picture = Picture.WithImage.mock

        // When
        val pictureModel = pictureModelMapper.transform(picture)

        // Then
        assertThat(pictureModel).isEqualTo(
            "https://image.tmdb.org/t/p/w500/6KErczPBROQty7QoIsaa6wJYXZi.jpg"
        )
    }

    @Test
    fun `given an empty picture when transform then return an empty string`() {
        // Given
        val picture = Picture.Empty

        // When
        val pictureModel = pictureModelMapper.transform(picture)

        // Then
        assertThat(pictureModel).isEqualTo("")
    }
}
