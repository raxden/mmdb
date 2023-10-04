package com.raxdenstudios.app.core.data.remote.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.model.VideoType
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.commons.treeten.test.rules.ThreeTenRule
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class VideoDtoToDomainMapperTest {

    @get:Rule
    val threeTenRule = ThreeTenRule()

    private val dateDtoToLocalDateMapper = DateDtoToLocalDateMapper()
    private lateinit var mapper: VideoDtoToDomainMapper

    @Before
    fun setUp() {
        mapper = VideoDtoToDomainMapper(dateDtoToLocalDateMapper)
    }

    @Test
    fun `given a videoDto when transform then return a video`() {
        val videoDto = VideoDto.mock

        val video = mapper.transform(videoDto)

        assertThat(video).isEqualTo(Video.mock)
    }

    @Test
    fun `given a videoDto clip when transform then return a video`() {
        val videoDto = VideoDto.mock.copy(type = "Clip")

        val video = mapper.transform(videoDto)

        assertThat(video).isEqualTo(Video.mock.copy(type = VideoType.Clip))
    }

    @Test
    fun `given a videoDto behind the scenes when transform then return a video`() {
        val videoDto = VideoDto.mock.copy(type = "Behind the Scenes")

        val video = mapper.transform(videoDto)

        assertThat(video).isEqualTo(Video.mock.copy(type = VideoType.BehindTheScenes))
    }

    @Test
    fun `given a videoDto teaser when transform then return a video`() {
        val videoDto = VideoDto.mock.copy(type = "Teaser")

        val video = mapper.transform(videoDto)

        assertThat(video).isEqualTo(Video.mock.copy(type = VideoType.Teaser))
    }
}
