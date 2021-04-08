package com.raxdenstudios.app.home

import androidx.lifecycle.DefaultLifecycleObserver

interface HomeNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit)
}
