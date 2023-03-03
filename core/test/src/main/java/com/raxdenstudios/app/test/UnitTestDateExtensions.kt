package com.raxdenstudios.app.test

import org.threeten.bp.zone.TzdbZoneRulesProvider
import org.threeten.bp.zone.ZoneRulesProvider

fun Any.initThreeTen() {
    if (ZoneRulesProvider.getAvailableZoneIds().isEmpty()) {
        val stream = this.javaClass.classLoader!!.getResourceAsStream("TZDB.dat")
        stream.use(::TzdbZoneRulesProvider).apply {
            ZoneRulesProvider.registerProvider(this)
        }
    }
}
