package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.app.core.i18n.R
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.provider.StringProvider
import org.threeten.bp.Duration
import javax.inject.Inject

class DurationModelMapper @Inject constructor(
    private val stringProvider: StringProvider,
) : DataMapper<Duration, String>() {

    override fun transform(source: Duration): String = when {
        source.toHours() > 1 && source.toMinutesPart() > 0 ->
            stringProvider.getString(R.string.hours_minutes, source.toHours(), source.toMinutesPart())
        source.toHours() > 1 ->
            stringProvider.getString(R.string.hours, source.toHours())
        source.toHours() >= 1 && source.toMinutesPart() > 0 ->
            stringProvider.getString(R.string.hour_minutes, source.toHours(), source.toMinutesPart())
        source.toHours() >= 1 ->
            stringProvider.getString(R.string.hour, source.toHours())
        source.toMinutes() > 1 ->
            stringProvider.getString(R.string.minutes, source.toMinutesPart())
        else -> ""
    }
}
