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
import com.raxdenstudios.commons.ext.safeLaunch
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

internal class HomeViewModel(
  private val dispatcher: DispatcherFacade,
  private val getHomeModulesUseCase: GetHomeModulesUseCase,
  private val getMoviesUseCase: GetMoviesUseCase,
  private val isAccountLogged: IsAccountLogged,
  private val addMovieToWatchList: AddMovieToWatchList,
  private val removeMovieFromWatchList: RemoveMovieFromWatchList,
  private val getMoviesUseCaseParamsMapper: GetMoviesUseCaseParamsMapper,
  private val homeModuleModelMapper: HomeModuleModelMapper
) : BaseViewModel() {

  private val mState = MutableLiveData<HomeUIState>()
  val state: LiveData<HomeUIState> = mState

  init {
    loadData()
  }

  private fun loadData() = viewModelScope.safeLaunch {
    mState.value = HomeUIState.Loading

    getHomeModulesUseCase.execute().collect { homeModuleList ->
      val accountLogged = isAccountLogged.execute()
      val homeModuleListModel = homeModuleList.map { homeModule ->
        async { getDataFromHomeModule(homeModule) }
      }.mapNotNull { deferred ->
        deferred.await()
      }
      val model = HomeModel(accountLogged, homeModuleListModel)
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
    when (val result = addMovieToWatchList.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success -> {
        val homeModelResult = updateMovieWithWatchButtonAndRefreshWatchListModule(
          homeModel = homeModel,
          homeModuleModel = homeModuleModel,
          carouselMoviesModel = carouselMoviesModel,
          movieListItemModel = movieListItemModel,
          watchButtonModel = WatchButtonModel.Selected,
        )
        mState.value = HomeUIState.Content(homeModelResult)
      }
    }
  }

  private suspend fun updateMovieWithWatchButtonAndRefreshWatchListModule(
    homeModel: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
    watchButtonModel: WatchButtonModel,
  ): HomeModel = withContext(dispatcher.default()) {
    val movieUpdated = movieListItemModel.copy(watchButtonModel = watchButtonModel)
    val carouselUpdated = carouselMoviesModel.replaceMovie(movieUpdated)
    val homeModuleUpdated = homeModuleModel.replaceCarousel(carouselUpdated)
    val homeModelUpdated = homeModel.replaceModule(homeModuleUpdated)
    val watchListModuleModel = getDataFromHomeModule(HomeModule.WatchListMovies)
    watchListModuleModel?.let { module ->
      homeModelUpdated.replaceModule(module)
    } ?: homeModelUpdated
  }

  fun removeMovieFromWatchList(
    homeModel: HomeModel,
    homeModuleModel: HomeModuleModel,
    carouselMoviesModel: CarouselMovieListModel,
    movieListItemModel: MovieListItemModel,
  ) = viewModelScope.safeLaunch {
    when (val result = removeMovieFromWatchList.execute(movieListItemModel.id)) {
      is ResultData.Error -> mState.value = HomeUIState.Error(result.throwable)
      is ResultData.Success -> {
        val homeModelResult = updateMovieWithWatchButtonAndRefreshWatchListModule(
          homeModel = homeModel,
          homeModuleModel = homeModuleModel,
          carouselMoviesModel = carouselMoviesModel,
          movieListItemModel = movieListItemModel,
          watchButtonModel = WatchButtonModel.Unselected,
        )
        mState.value = HomeUIState.Content(homeModelResult)
      }
    }
  }
}
