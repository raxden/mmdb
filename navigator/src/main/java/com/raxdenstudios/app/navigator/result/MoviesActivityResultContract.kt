package com.raxdenstudios.app.navigator.result

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.raxdenstudios.app.list.view.MovieListActivity
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.movie.domain.model.SearchType

internal class MoviesActivityResultContract : ActivityResultContract<SearchType, Boolean>() {

  override fun createIntent(context: Context, searchType: SearchType): Intent {
    val params = MovieListParams(searchType)
    return MovieListActivity.createIntent(context, params)
  }

  override fun parseResult(resultCode: Int, intent: Intent?): Boolean =
    resultCode == Activity.RESULT_OK
}
