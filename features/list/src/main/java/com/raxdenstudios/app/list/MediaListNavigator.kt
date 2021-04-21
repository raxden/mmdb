package com.raxdenstudios.app.list

import androidx.lifecycle.DefaultLifecycleObserver

interface MediaListNavigator : DefaultLifecycleObserver {

  fun login(onSuccess: () -> Unit = {})
}