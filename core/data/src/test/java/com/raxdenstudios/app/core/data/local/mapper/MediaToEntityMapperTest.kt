package com.raxdenstudios.app.core.data.local.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.database.model.MediaEntity
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.test.ThreeTenRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class MediaToEntityMapperTest {

    @get:Rule
    val threeTenRule = ThreeTenRule()

    private val sizeToEntityMapper = SizeToEntityMapper()
    private val pictureToEntityMapper = PictureToEntityMapper(
        sizeToEntityMapper = sizeToEntityMapper
    )
    private val voteToEntityMapper = VoteToEntityMapper()
    private lateinit var mediaToEntityMapper: MediaToEntityMapper

    @Before
    fun setUp() {
        mediaToEntityMapper = MediaToEntityMapper(
            pictureToEntityMapper = pictureToEntityMapper,
            voteToEntityMapper = voteToEntityMapper
        )
    }

    @Test
    fun `given a movie when transform then return a movie entity`() {
        val movie = Media.Movie.mock

        val entity = mediaToEntityMapper.transform(movie)

        assertThat(entity).isEqualTo(MediaEntity.mock)
    }

    @Test
    fun `given a tvShow when transform then return a movie entity`() {
        val tvShow = Media.TVShow.mock

        val entity = mediaToEntityMapper.transform(tvShow)

        assertThat(entity).isEqualTo(MediaEntity.mock.copy(type = 2))
    }
}
