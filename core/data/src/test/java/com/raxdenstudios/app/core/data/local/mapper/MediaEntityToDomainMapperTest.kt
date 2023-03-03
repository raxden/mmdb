package com.raxdenstudios.app.core.data.local.mapper

import com.google.common.truth.Truth
import com.raxdenstudios.app.core.database.model.MediaEntity
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.test.ThreeTenRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MediaEntityToDomainMapperTest {

    @get:Rule
    val threeTenRule = ThreeTenRule()

    private val sizeEntityToDomainMapper = SizeEntityToDomainMapper()
    private val pictureEntityToDomainMapper = PictureEntityToDomainMapper(
        sizeEntityToDomainMapper = sizeEntityToDomainMapper
    )
    private val voteEntityToDomainMapper = VoteEntityToDomainMapper()
    private lateinit var mediaEntityToDomainMapper: MediaEntityToDomainMapper

    @Before
    fun setUp() {
        mediaEntityToDomainMapper = MediaEntityToDomainMapper(
            pictureEntityToDomainMapper = pictureEntityToDomainMapper,
            voteEntityToDomainMapper = voteEntityToDomainMapper,
        )
    }

    @Test
    fun `given a movie entity when transform then return a movie`() {
        val movie = MediaEntity.mock

        val entity = mediaEntityToDomainMapper.transform(movie)

        Truth.assertThat(entity).isEqualTo(
            Media.Movie.mock.copy(genres = emptyList(), certification = "")
        )
    }

    @Test
    fun `given a tvShow entity when transform then return a movie entity`() {
        val tvShow = MediaEntity.mock.copy(type = 2)

        val entity = mediaEntityToDomainMapper.transform(tvShow)

        Truth.assertThat(entity).isEqualTo(
            Media.TVShow.mock.copy(genres = emptyList(), certification = "")
        )
    }
}
