package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.commons.core.util.DataMapper
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

class RatingModelMapper @Inject constructor() : DataMapper<Float, String>() {

    private val decimalFormat = DecimalFormat("#.##").apply {
        roundingMode = RoundingMode.DOWN
    }

    override fun transform(source: Float): String = decimalFormat.format(source)
}
