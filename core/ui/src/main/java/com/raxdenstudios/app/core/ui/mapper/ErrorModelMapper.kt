package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

class ErrorModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
) {

    @Suppress("UNUSED_PARAMETER")
    fun transform(source: Throwable): ErrorModel =
        ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )

    fun transform(source: ErrorDomain): ErrorModel = when (source) {
        is ErrorDomain.Network.Client -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = source.message,
        )
        is ErrorDomain.Network.Connection -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_lost_connection),
        )
        is ErrorDomain.Network.Server -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = source.message,
        )
        is ErrorDomain.Network.Unknown -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
        is ErrorDomain.ResourceNotFound -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
        is ErrorDomain.Unauthorized -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
        is ErrorDomain.Unknown -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
        else -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
    }
}
