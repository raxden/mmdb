package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.commons.core.util.DataMapper
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle
import javax.inject.Inject

class DateModelMapper @Inject constructor() : DataMapper<LocalDate, String>() {

    override fun transform(source: LocalDate): String {
        return DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(source)
    }
}
