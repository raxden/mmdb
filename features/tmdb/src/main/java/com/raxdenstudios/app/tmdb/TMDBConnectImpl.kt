package com.raxdenstudios.app.tmdb

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.tmdb.view.TMDBConnectFragment
import com.raxdenstudios.commons.ext.loadDialogFragment

class TMDBConnectImpl(
  private val activity: FragmentActivity
) : TMDBConnect {

  companion object {
    private val TAG = TMDBConnectFragment::class.java.simpleName
  }

  override fun sigIn(
    onSuccess: (credentials: Credentials) -> Unit,
    onError: (throwable: Throwable) -> Unit
  ) {
    activity.loadDialogFragment(TAG, true) {
      TMDBConnectFragment.newInstance()
    }.also { fragment ->
      fragment.onSuccess = onSuccess
      fragment.onError = onError
    }
  }
}
