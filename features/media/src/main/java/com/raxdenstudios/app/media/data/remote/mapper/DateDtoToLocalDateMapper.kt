package com.raxdenstudios.app.media.data.remote.mapper

import com.raxdenstudios.commons.util.DataMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

internal class DateDtoToLocalDateMapper : DataMapper<String, LocalDate>() {

  override fun transform(source: String): LocalDate =
    LocalDate.parse(source, DateTimeFormatter.ISO_LOCAL_DATE)
}
