package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.commons.core.util.DataMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import javax.inject.Inject

class DateDtoToLocalDateMapper @Inject constructor() : DataMapper<String, LocalDate>() {

    @Suppress("MagicNumber")
    override fun transform(source: String): LocalDate =
        kotlin.runCatching { LocalDate.parse(source, DateTimeFormatter.ISO_DATE) }
            .getOrDefault(LocalDate.of(1970, 1, 1))
}
