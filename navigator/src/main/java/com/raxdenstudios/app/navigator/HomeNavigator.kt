package com.raxdenstudios.app.navigator

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MoviesActivityResultContract

internal class HomeNavigatorImpl(
  private val activity: FragmentActivity
) : HomeNavigator {

  companion object {
    private const val LOGIN_KEY = "login"
    private const val MOVIES_KEY = "movies"
  }

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Unit>
  private lateinit var moviesActivityResultLauncher: ActivityResultLauncher<SearchType>

  private var onLoginSuccess: () -> Unit = {}
  private var onMoviesRefresh: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    loginActivityResultLauncher = registry.registerLoginActivityResultRegistry(owner)
    moviesActivityResultLauncher = registry.registerMoviesActivityResultRegistry(owner)
  }

  override fun login(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }

  override fun movies(homeModuleModel: HomeModuleModel, onRefresh: () -> Unit) {
    onMoviesRefresh = onRefresh
    val searchType = when (homeModuleModel) {
      is HomeModuleModel.CarouselMovies.NowPlaying -> SearchType.NowPlaying
      is HomeModuleModel.CarouselMovies.Popular -> SearchType.Popular
      is HomeModuleModel.CarouselMovies.TopRated -> SearchType.TopRated
      is HomeModuleModel.CarouselMovies.Upcoming -> SearchType.Upcoming
      is HomeModuleModel.WatchList -> SearchType.WatchList
    }
    moviesActivityResultLauncher.launch(searchType)
  }

  private fun ActivityResultRegistry.registerLoginActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<Unit> =
    register(
      LOGIN_KEY,
      owner,
      LoginActivityResultContract()
    ) { logged -> if (logged) onLoginSuccess() }

  private fun ActivityResultRegistry.registerMoviesActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<SearchType> =
    register(
      MOVIES_KEY,
      owner,
      MoviesActivityResultContract()
    ) { refresh -> if (refresh) onMoviesRefresh() }
}
