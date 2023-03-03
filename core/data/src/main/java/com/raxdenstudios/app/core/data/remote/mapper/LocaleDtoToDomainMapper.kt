package com.raxdenstudios.app.core.data.remote.mapper

import java.util.Locale
import javax.inject.Inject

class LocaleDtoToDomainMapper @Inject constructor() {

    fun transform(source: String): Locale =
        Locale(source)
}
