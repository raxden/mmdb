package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.NetworkErrorDto
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.NetworkError
import com.raxdenstudios.app.core.model.ErrorDomain
import javax.inject.Inject

class NetworkErrorDtoToErrorMapper @Inject constructor() : DataMapper<NetworkErrorDto, ErrorDomain>() {

    override fun transform(source: NetworkErrorDto): ErrorDomain = when (source) {
        is NetworkError.Client -> ErrorDomain.Network.Client(source.message)
        is NetworkError.Network -> ErrorDomain.Network.Connection(source.message)
        is NetworkError.Server -> ErrorDomain.Network.Server(source.message)
        is NetworkError.Unknown -> ErrorDomain.Network.Unknown(source.message)
    }
}
