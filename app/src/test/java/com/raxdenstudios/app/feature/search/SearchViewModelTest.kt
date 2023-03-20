package com.raxdenstudios.app.feature.search

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.domain.AddMediaToWatchlistUseCase
import com.raxdenstudios.app.core.domain.RemoveMediaFromWatchlistUseCase
import com.raxdenstudios.app.core.domain.SearchMediasUseCase
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.ui.mapper.CurrencyModelMapper
import com.raxdenstudios.app.core.ui.mapper.DateModelMapper
import com.raxdenstudios.app.core.ui.mapper.DurationModelMapper
import com.raxdenstudios.app.core.ui.mapper.ErrorModelMapper
import com.raxdenstudios.app.core.ui.mapper.LanguageModelMapper
import com.raxdenstudios.app.core.ui.mapper.MediaModelMapper
import com.raxdenstudios.app.core.ui.mapper.PageListMediaModelMapper
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.app.core.ui.model.MediaModel
import com.raxdenstudios.app.feature.search.model.SearchBarModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val searchMediasUseCase: SearchMediasUseCase = mockk()
    private val addMediaToWatchlistUseCase: AddMediaToWatchlistUseCase = mockk()
    private val removeMediaFromWatchlistUseCase: RemoveMediaFromWatchlistUseCase = mockk()
    private val stringProvider: StringProvider = mockk(relaxed = true)
    private val durationModelMapper = DurationModelMapper(
        stringProvider = stringProvider
    )
    private val dateModelMapper = DateModelMapper()
    private val languageModelMapper = LanguageModelMapper()
    private val currencyModelMapper = CurrencyModelMapper()
    private val mediaModelMapper = MediaModelMapper(
        durationModelMapper = durationModelMapper,
        dateModelMapper = dateModelMapper,
        languageModelMapper = languageModelMapper,
        currencyModelMapper = currencyModelMapper,
    )
    private val errorModelMapper = ErrorModelMapper(
        stringProvider = stringProvider
    )
    private val pageListMediaModelMapper = PageListMediaModelMapper(
        mediaModelMapper = mediaModelMapper,
        errorModelMapper = errorModelMapper,
    )
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        viewModel = SearchViewModel(
            searchMediasUseCase = searchMediasUseCase,
            addMediaToWatchlistUseCase = addMediaToWatchlistUseCase,
            removeMediaFromWatchlistUseCase = removeMediaFromWatchlistUseCase,
            pageListMediaModelMapper = pageListMediaModelMapper,
        )
    }

    @Test
    fun `should search medias`() = runTest {
        val query = "query"
        coEvery { searchMediasUseCase.invoke(any()) } returns flowOf(ResultData.Success(results))

        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(SearchContract.UserEvent.SearchBarQueryChanged(query))

            val firstResult = awaitItem()
            assertThat(firstResult).isEqualTo(
                SearchContract.UIState(
                    searchBarModel = SearchBarModel.Searching(query),
                )
            )
            val secondResult = awaitItem()
            assertThat(secondResult).isEqualTo(
                SearchContract.UIState(
                    searchBarModel = SearchBarModel.WithResults(query = query),
                    results = listOf(
                        MediaModel.mock.copy(mediaType = MediaType.Movie),
                        MediaModel.mock.copy(mediaType = MediaType.TvShow),
                    ),
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given an error, When search is performed, Then show error`() = runTest {
        val query = "query"
        coEvery { searchMediasUseCase.invoke(any()) } returns flowOf(ResultData.Failure(ErrorDomain.Unknown("")))

        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(SearchContract.UserEvent.SearchBarQueryChanged(query))

            val firstResult = awaitItem()
            assertThat(firstResult).isEqualTo(
                SearchContract.UIState(
                    searchBarModel = SearchBarModel.Searching(query),
                )
            )
            val secondResult = awaitItem()
            assertThat(secondResult).isEqualTo(
                SearchContract.UIState(
                    searchBarModel = SearchBarModel.WithoutResults(query = query),
                    results = emptyList(),
                    error = ErrorModel.mock,
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should clear search`() = runTest {
        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(SearchContract.UserEvent.ClearSearchBarClicked)

            val firstResult = awaitItem()
            assertThat(firstResult).isEqualTo(
                SearchContract.UIState(
                    searchBarModel = SearchBarModel.Focused,
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `should navigate to media`() = runTest {
        val media = MediaModel.mock
        viewModel.uiState.test {
            skipItems(1)

            viewModel.setUserEvent(SearchContract.UserEvent.MediaClicked(media))

            val firstResult = awaitItem()
            assertThat(firstResult).isEqualTo(
                SearchContract.UIState(
                    events = setOf(SearchContract.UIEvent.NavigateToMedia(media.id, media.mediaType)),
                )
            )
            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {

        private val results = PageList(
            items = listOf(
                Media.Movie.mock,
                Media.TVShow.mock,
            ),
            page = Page(1),
        )
    }
}
