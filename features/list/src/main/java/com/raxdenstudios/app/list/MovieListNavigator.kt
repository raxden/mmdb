package com.raxdenstudios.app.list

import androidx.lifecycle.DefaultLifecycleObserver

interface MovieListNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit = {})
}