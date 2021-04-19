package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.model.Account
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
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.safeLaunch
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

internal class HomeViewModel(
  private val dispatcher: DispatcherFacade,
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMoviesUseCase: GetMoviesUseCase,
  private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase,
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

    getHomeModulesUseCase.execute().collect { result ->
      val account = result.first
      val modules = result.second
      val homeModuleListModel = modules.map { homeModule ->
        async { getDataFromHomeModule(homeModule) }
      }.mapNotNull { deferred ->
        deferred.await()
      }
      val model = HomeModel(account is Account.Logged, homeModuleListModel)
      mState.value = HomeUIState.Content(model)
    }
  }

  private suspend fun getDataFromHomeModule(module: HomeModule): HomeModuleModel? {
    val useCaseParams = getMoviesUseCaseParamsMapper.transform(module)
    val resultData = getMoviesUseCase.execute(useCaseParams)
    return homeModuleModelMapper.transform(module, resultData)
  }

  fun refreshData() {
    loadData()
  }

  fun movieSelected(
    model: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieItemModel: MovieListItemModel,
  ) {

  }

  fun addMovieToWatchList(
    homeModel: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
  ) = viewModelScope.safeLaunch {
    when (val result = addMovieToWatchListUseCase.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success -> {
        val homeModelResult = updateMovieWithWatchButton(
          homeModel = homeModel,
          homeModuleModel = homeModuleModel,
          carouselMoviesModel = carouselMoviesModel,
          movieListItemModel = movieListItemModel,
          watchButtonModel = WatchButtonModel.Selected,
        )
        mState.value = HomeUIState.Content(homeModelResult)
        refreshData()
      }
    }
  }

  private suspend fun updateMovieWithWatchButton(
    homeModel: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
    watchButtonModel: WatchButtonModel,
  ): HomeModel = withContext(dispatcher.default()) {
    val movieUpdated = movieListItemModel.copy(watchButtonModel = watchButtonModel)
    val carouselUpdated = carouselMoviesModel.replaceMovie(movieUpdated)
    val homeModuleUpdated = homeModuleModel.replaceCarousel(carouselUpdated)
    homeModel.replaceModule(homeModuleUpdated)
  }

  fun removeMovieFromWatchList(
    homeModel: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
  ) = viewModelScope.safeLaunch {
    when (val result = removeMovieFromWatchListUseCase.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success -> {
        val homeModelResult = updateMovieWithWatchButton(
          homeModel = homeModel,
          homeModuleModel = homeModuleModel,
          carouselMoviesModel = carouselMoviesModel,
          movieListItemModel = movieListItemModel,
          watchButtonModel = WatchButtonModel.Unselected,
        )
        mState.value = HomeUIState.Content(homeModelResult)
        refreshData()
      }
    }
  }
}
