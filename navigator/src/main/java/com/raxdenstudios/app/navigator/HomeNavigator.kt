package com.raxdenstudios.app.navigator

import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.list.view.MovieListActivity
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.commons.ext.startActivity

internal class HomeNavigatorImpl(
  private val activity: FragmentActivity
) : HomeNavigator {

  companion object {
    private const val LOGIN_KEY = "login"
  }

  private val registry: ActivityResultRegistry = activity.activityResultRegistry
  private lateinit var loginActivityResultLauncher: ActivityResultLauncher<Unit>

  private var onLoginSuccess: () -> Unit = {}

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    loginActivityResultLauncher = registry.registerLoginActivityResultRegistry(owner)
  }

  override fun login(onSuccess: () -> Unit) {
    onLoginSuccess = onSuccess
    loginActivityResultLauncher.launch(Unit)
  }

  override fun movies(homeModuleModel: HomeModuleModel) {
    val searchType = when (homeModuleModel) {
      is HomeModuleModel.CarouselMovies.NowPlaying -> SearchType.NowPlaying
      is HomeModuleModel.CarouselMovies.Popular -> SearchType.Popular
      is HomeModuleModel.CarouselMovies.TopRated -> SearchType.TopRated
      is HomeModuleModel.CarouselMovies.Upcoming -> SearchType.Upcoming
      is HomeModuleModel.WatchList -> SearchType.WatchList
    }
    val params = MovieListParams(searchType)
    MovieListActivity.createIntent(activity, params).startActivity(activity)
  }

  private fun ActivityResultRegistry.registerLoginActivityResultRegistry(
    owner: LifecycleOwner
  ): ActivityResultLauncher<Unit> =
    register(
      LOGIN_KEY,
      owner,
      LoginActivityResultContract()
    ) { logged -> if (logged) onLoginSuccess() }
}
