package com.raxdenstudios.app.error

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.commons.ext.showSimpleDialog
import com.raxdenstudios.commons.retrofit.NetworkException
import timber.log.Timber

internal class ErrorManagerImpl(
  private val activity: FragmentActivity,
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
      activity.getString(R.string.feature_error_information),
      exception.message,
    )
  }

  private fun handleClientException(exception: NetworkException.Client) {
    showErrorDialog(
      activity.getString(R.string.feature_error_information),
      exception.message,
    )
  }

  private fun handleUnknownException() {
    showErrorDialog(
      activity.getString(R.string.feature_error_information),
      activity.getString(R.string.feature_error_unespected_error_message)
    )
  }

  private fun handleNetworkException() {
    showErrorDialog(
      activity.getString(R.string.feature_error_information),
      activity.getString(R.string.feature_error_lost_connection)
    )
  }

  private fun showErrorDialog(title: String, message: String) {
    activity.showSimpleDialog(title, message)
  }
}
