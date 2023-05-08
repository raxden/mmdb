package com.raxdenstudios.app.core.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class MediaModelMapperTest {

    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val durationModelMapper: DurationModelMapper = DurationModelMapper(stringProvider)
    private val dateModelMapper: DateModelMapper = DateModelMapper()
    private val languageModelMapper: LanguageModelMapper = LanguageModelMapper()
    private val currencyModelMapper: CurrencyModelMapper = CurrencyModelMapper()
    private val ratingModelMapper: RatingModelMapper = RatingModelMapper()
    private val pictureModelMapper: PictureModelMapper = PictureModelMapper()
    private lateinit var mediaModelMapper: MediaModelMapper

    @Before
    fun setUp() {
        mediaModelMapper = MediaModelMapper(
            durationModelMapper = durationModelMapper,
            dateModelMapper = dateModelMapper,
            languageModelMapper = languageModelMapper,
            currencyModelMapper = currencyModelMapper,
            ratingModelMapper = ratingModelMapper,
            pictureModelMapper = pictureModelMapper,
        )
    }

    @Test
    fun `given a movie when transform then return a model`() {
        val movie = Media.Movie.mock

        val result = mediaModelMapper.transform(movie)

        assertThat(result).isEqualTo(MediaModel.mock)
    }

    @Test
    fun `given a tvshow when transform then return a model`() {
        val movie = Media.TVShow.mock

        val result = mediaModelMapper.transform(movie)

        assertThat(result).isEqualTo(MediaModel.mock.copy(mediaType = MediaType.TvShow))
    }
}
