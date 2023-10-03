package com.raxdenstudios.app.core.data.local.mapper

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.commons.core.util.DataMapper
import javax.inject.Inject

class ExceptionToErrorMapper @Inject constructor() : DataMapper<Throwable, ErrorDomain>() {

    override fun transform(source: Throwable): ErrorDomain =
        ErrorDomain.Unknown(source.message ?: "Unknown error")
}
