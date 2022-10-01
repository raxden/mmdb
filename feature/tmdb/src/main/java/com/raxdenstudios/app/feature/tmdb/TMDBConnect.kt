package com.raxdenstudios.app.feature.tmdb

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.core.model.Credentials
import com.raxdenstudios.commons.ext.loadDialogFragment
import javax.inject.Inject

class TMDBConnect @Inject constructor(
    private val activity: FragmentActivity,
) {

    companion object {

        private val TAG = TMDBConnectFragment::class.java.simpleName
    }

    fun sigIn(
//        onSuccess: (credentials: Credentials) -> Unit,
//        onError: (throwable: Throwable) -> Unit,
    ) {
        activity.loadDialogFragment(TAG, true) {
            TMDBConnectFragment.newInstance()
        }.also { fragment ->
//            fragment.onSuccess = onSuccess
//            fragment.onError = onError
        }
    }
}
