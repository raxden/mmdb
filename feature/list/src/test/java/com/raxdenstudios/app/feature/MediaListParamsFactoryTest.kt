package com.raxdenstudios.app.feature

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.feature.model.MediaListParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

internal class MediaListParamsFactoryTest {

    private val savedStateHandle: SavedStateHandle = mockk()
    private lateinit var factory: MediaListParamsFactory

    @Before
    fun setUp() {
        factory = MediaListParamsFactory(
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `should create MediaListParams_Related`() {
        every { savedStateHandle.contains(MediaListParamsFactory.MEDIA_ID) } returns true
        every { savedStateHandle.get<Long>(MediaListParamsFactory.MEDIA_ID) } returns 1L
        every { savedStateHandle.get<Int>(MediaListParamsFactory.MEDIA_TYPE_PARAM) } returns 1

        val params = factory.create()

        assertThat(params).isEqualTo(
            MediaListParams.Related(
                mediaId = MediaId(1L),
                mediaType = MediaType.Movie
            )
        )
    }

    @Test
    fun `should create MediaListParams_List`() {
        every { savedStateHandle.contains(MediaListParamsFactory.MEDIA_ID) } returns false
        every { savedStateHandle.get<Int>(MediaListParamsFactory.MEDIA_TYPE_PARAM) } returns 1
        every { savedStateHandle.get<Int>(MediaListParamsFactory.MEDIA_CATEGORY_PARAM) } returns 1

        val params = factory.create()

        assertThat(params).isEqualTo(
            MediaListParams.List(
                mediaFilter = MediaFilter(
                    mediaType = MediaType.Movie,
                    mediaCategory = MediaCategory.NowPlaying
                )
            )
        )
    }
}
