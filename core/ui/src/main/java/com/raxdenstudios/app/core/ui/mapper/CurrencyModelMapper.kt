package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.commons.DataMapper
import java.text.NumberFormat
import java.util.Currency
import javax.inject.Inject

class CurrencyModelMapper @Inject constructor() : DataMapper<Double, String>() {

    override fun transform(source: Double): String {
        if (source == 0.0) return ""
        val currencyFormat = NumberFormat.getCurrencyInstance()
        currencyFormat.currency = Currency.getInstance("USD")
        return currencyFormat.format(source)
    }
}
