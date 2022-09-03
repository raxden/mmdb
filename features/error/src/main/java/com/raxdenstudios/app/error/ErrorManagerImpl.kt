package com.raxdenstudios.app.error

import android.content.Context
import com.raxdenstudios.commons.ext.showSimpleDialog
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.retrofit.NetworkException
import dagger.hilt.android.qualifiers.ApplicationContext
import timber.log.Timber
import javax.inject.Inject

internal class ErrorManagerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val stringProvider: StringProvider,
) : ErrorManager {

    override fun handleError(throwable: Throwable) {
        when (throwable) {
            is NetworkException.Server -> handleServerException(throwable)
            is NetworkException.Client -> handleClientException(throwable)
            is NetworkException -> handleNetworkException()
            is NetworkException.Unknown -> handleUnknownException()
        }
        Timber.e(throwable)
    }

    private fun handleServerException(exception: NetworkException.Server) {
        showErrorDialog(
            stringProvider.getString(R.string.feature_error_information),
            exception.message,
        )
    }

    private fun handleClientException(exception: NetworkException.Client) {
        showErrorDialog(
            stringProvider.getString(R.string.feature_error_information),
            exception.message,
        )
    }

    private fun handleUnknownException() {
        showErrorDialog(
            stringProvider.getString(R.string.feature_error_information),
            stringProvider.getString(R.string.feature_error_unespected_error_message)
        )
    }

    private fun handleNetworkException() {
        showErrorDialog(
            stringProvider.getString(R.string.feature_error_information),
            stringProvider.getString(R.string.feature_error_lost_connection)
        )
    }

    private fun showErrorDialog(title: String, message: String) {
        context.showSimpleDialog(title, message)
    }
}
