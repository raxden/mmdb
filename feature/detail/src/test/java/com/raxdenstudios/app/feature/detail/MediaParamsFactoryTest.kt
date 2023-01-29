package com.raxdenstudios.app.feature.detail

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.feature.detail.model.MediaParams
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

internal class MediaParamsFactoryTest {

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
    private lateinit var factory: MediaParamsFactory

    @Before
    fun setUp() {
        factory = MediaParamsFactory(
            savedStateHandle = savedStateHandle
        )
    }

    @Test
    fun `should create MediaParams`() {
        every { savedStateHandle.get<Long>("mediaId") } returns 1L
        every { savedStateHandle.get<Int>("mediaType") } returns 1

        val params = factory.create()

        assertThat(params).isEqualTo(
            MediaParams(
                mediaId = MediaId(1L),
                mediaType = MediaType.Movie
            )
        )
    }
}
