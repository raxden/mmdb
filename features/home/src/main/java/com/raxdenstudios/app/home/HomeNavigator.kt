package com.raxdenstudios.app.home

import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.app.movie.view.model.MediaFilterModel

interface HomeNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit = {})
  fun movies(mediaFilterModel: MediaFilterModel, onRefresh: () -> Unit = {})
}
