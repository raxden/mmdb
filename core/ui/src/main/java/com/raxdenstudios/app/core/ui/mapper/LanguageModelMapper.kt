package com.raxdenstudios.app.core.ui.mapper

import com.raxdenstudios.commons.DataMapper
import java.util.Locale
import javax.inject.Inject

class LanguageModelMapper @Inject constructor() : DataMapper<Locale, String>() {

    override fun transform(source: Locale): String {
        return source.getDisplayLanguage(Locale.getDefault())
    }
}
