package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.IsAccountLogged
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.mapper.GetMoviesUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.model.*
import com.raxdenstudios.app.movie.domain.AddMovieToWatchList
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchList
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ext.launch
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect

internal class HomeViewModel(
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMoviesUseCase: GetMoviesUseCase,
  private val isAccountLogged: IsAccountLogged,
  private val addMovieToWatchList: AddMovieToWatchList,
  private val removeMovieFromWatchList: RemoveMovieFromWatchList,
  private val getMoviesUseCaseParamsMapper: GetMoviesUseCaseParamsMapper,
  private val homeModuleModelMapper: HomeModuleModelMapper,
) : BaseViewModel() {

  private val mState = MutableLiveData<HomeUIState>()
  val state: LiveData<HomeUIState> = mState

  init {
    loadData()
  }

  private fun loadData() = viewModelScope.launch {
    mState.value = HomeUIState.Loading

    getHomeModulesUseCase.execute().collect { homeModuleList ->
      val accountLogged = isAccountLogged.execute()
      val moduleModelList = homeModuleList.map { homeModule ->
        async { getMoviesFromModule(homeModule) }
      }.map { deferred ->
        deferred.await()
      }
      val model = HomeModel(accountLogged, moduleModelList)
      mState.value = HomeUIState.Content(model)
    }
  }

  private suspend fun getMoviesFromModule(homeModule: HomeModule): HomeModuleModel {
    val useCaseParams = getMoviesUseCaseParamsMapper.transform(homeModule)
    val resultData = getMoviesUseCase.execute(useCaseParams)
    return homeModuleModelMapper.transform(homeModule, resultData)
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
    model: HomeModel,
    homeModuleModel: HomeModuleModel.CarouselMovies,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
  ) = viewModelScope.launch {
    when (val result = addMovieToWatchList.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success ->
        updateMovieWithWatchButton(
          model,
          homeModuleModel,
          carouselMoviesModel,
          movieListItemModel,
          WatchButtonModel.Selected
        )
    }
  }

  private fun updateMovieWithWatchButton(
    model: HomeModel,
    homeModuleModel: HomeModuleModel.CarouselMovies,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
    watchButtonModel: WatchButtonModel,
  ) {
    val movie = movieListItemModel.copy(watchButtonModel = watchButtonModel)
    val home = replaceMovieFromHomeModel(movie, carouselMoviesModel, homeModuleModel, model)
    mState.value = HomeUIState.Content(home)
  }

  fun removeMovieFromWatchList(
    model: HomeModel,
    homeModuleModel: HomeModuleModel.CarouselMovies,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
  ) = viewModelScope.launch {
    when (val result = removeMovieFromWatchList.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success ->
        updateMovieWithWatchButton(
          model,
          homeModuleModel,
          carouselMoviesModel,
          movieListItemModel,
          WatchButtonModel.Unselected
        )
    }
  }

  private fun replaceMovieFromHomeModel(
    movieListItemModel: MovieListItemModel,
    carouselMoviesModel: CarouselMovieListModel,
    homeModuleModel: HomeModuleModel.CarouselMovies,
    model: HomeModel
  ): HomeModel {
    val carousel = carouselMoviesModel.replaceMovie(movieListItemModel)
    val homeModule = homeModuleModel.replaceCarousel(carousel)
    return model.replaceModule(homeModule)
  }
}
