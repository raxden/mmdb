package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.mapper.GetMoviesUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.movie.domain.AddMovieToWatchListUseCase
import com.raxdenstudios.app.movie.domain.GetMoviesUseCase
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchListUseCase
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ext.replaceItem
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

internal class HomeViewModel(
  private val dispatcher: DispatcherFacade,
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMoviesUseCase: GetMoviesUseCase,
  private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase,
  private val isAccountLoggedUseCase: IsAccountLoggedUseCase,
  private val removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase,
  private val getMoviesUseCaseParamsMapper: GetMoviesUseCaseParamsMapper,
  private val homeModuleModelMapper: HomeModuleModelMapper,
) : BaseViewModel() {

  private val mState = MutableLiveData<HomeUIState>()
  val state: LiveData<HomeUIState> = mState

  init {
    loadData()
  }

  private fun loadData() = viewModelScope.safeLaunch {
    mState.value = HomeUIState.Loading

    getHomeModulesUseCase.execute().collect { modules ->
      val accountIsLogged = isAccountLoggedUseCase.execute()
      val homeModuleListModel = modules.map { homeModule ->
        async { getDataFromModule(homeModule) }
      }.mapNotNull { deferred ->
        deferred.await()
      }
      val model = HomeModel(accountIsLogged, homeModuleListModel)
      mState.value = HomeUIState.Content(model)
    }
  }

  private suspend fun getDataFromModule(module: HomeModule): HomeModuleModel? {
    val useCaseParams = getMoviesUseCaseParamsMapper.transform(module)
    val resultData = getMoviesUseCase.execute(useCaseParams)
    return homeModuleModelMapper.transform(module, resultData)
  }

  fun refreshData() {
    loadData()
  }

  fun movieSelected(
    model: HomeModel,
    homeModuleModel: HomeModuleModel.CarouselMovies,
    carouselMoviesModel: CarouselMovieListModel,
    movieItemModel: MovieListItemModel,
  ) {

  }

  fun addMovieToWatchList(
    home: HomeModel,
    homeModule: HomeModuleModel.CarouselMovies,
    carouselMovieList: CarouselMovieListModel,
    movieListItem: MovieListItemModel,
  ) = viewModelScope.safeLaunch {
    updateMovieWithWatchButton(
      home = home,
      homeModule = homeModule,
      carouselMovieList = carouselMovieList,
      movieListItem = movieListItem,
      watchButton = WatchButtonModel.Selected,
    )
    addMovieToWatchListUseCase.execute(
      AddMovieToWatchListUseCase.Params(movieListItem.id, movieListItem.mediaType)
    ).onSuccess { refreshData() }
  }

  fun removeMovieFromWatchList(
    home: HomeModel,
    homeModule: HomeModuleModel.CarouselMovies,
    carouselMovieList: CarouselMovieListModel,
    movieListItem: MovieListItemModel,
  ) = viewModelScope.safeLaunch {
    updateMovieWithWatchButton(
      home = home,
      homeModule = homeModule,
      carouselMovieList = carouselMovieList,
      movieListItem = movieListItem,
      watchButton = WatchButtonModel.Unselected,
    )
    removeMovieFromWatchListUseCase.execute(
      RemoveMovieFromWatchListUseCase.Params(movieListItem.id, movieListItem.mediaType)
    ).onSuccess { refreshData() }
  }

  private suspend fun updateMovieWithWatchButton(
    home: HomeModel,
    homeModule: HomeModuleModel.CarouselMovies,
    carouselMovieList: CarouselMovieListModel,
    movieListItem: MovieListItemModel,
    watchButton: WatchButtonModel,
  ) {
    val homeUpdated = withContext(dispatcher.default()) {
      val movieListItemUpdated = movieListItem.copy(watchButtonModel = watchButton)
      val carouselMovieListUpdated = carouselMovieList.replaceMovie(movieListItemUpdated)
      val homeModuleUpdated = homeModule.copy(carouselMovieListModel = carouselMovieListUpdated)
      home.copy(modules = home.modules.replaceItem(homeModuleUpdated) { it == homeModule })
    }
    mState.value = HomeUIState.Content(homeUpdated)
  }
}
