package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.commons.core.util.DataMapper
import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.network.model.ErrorDto
import javax.inject.Inject

class NetworkErrorDtoToErrorMapper @Inject constructor() : DataMapper<NetworkError<ErrorDto>, ErrorDomain>() {

    override fun transform(source: NetworkError<ErrorDto>): ErrorDomain = when (source) {
        is NetworkError.Client -> ErrorDomain.Network.Client(source.message)
        is NetworkError.Network -> ErrorDomain.Network.Connection(source.message)
        is NetworkError.Server -> ErrorDomain.Network.Server(source.message)
        is NetworkError.Unknown -> ErrorDomain.Network.Unknown(source.message)
    }
}
