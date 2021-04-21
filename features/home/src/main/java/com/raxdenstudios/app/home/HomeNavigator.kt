package com.raxdenstudios.app.home

import androidx.lifecycle.DefaultLifecycleObserver
import com.raxdenstudios.app.media.view.model.MediaFilterModel

interface HomeNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit = {})
  fun movies(mediaFilterModel: MediaFilterModel, onRefresh: () -> Unit = {})
}
