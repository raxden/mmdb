package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.ui.R
import com.raxdenstudios.app.core.ui.model.ErrorModel
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.provider.StringProvider
import com.raxdenstudios.commons.retrofit.NetworkException
import javax.inject.Inject

class ErrorModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
) : DataMapper<Throwable, ErrorModel>() {

    override fun transform(source: Throwable): ErrorModel = when (source) {
        is NetworkException.Server -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = source.message,
        )
        is NetworkException.Client -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = source.message,
        )
        is NetworkException.Unknown -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
        is NetworkException -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_lost_connection),
        )
        else -> ErrorModel(
            title = stringProvider.getString(R.string.error_information),
            message = stringProvider.getString(R.string.error_unespected_error_message),
        )
    }
}
