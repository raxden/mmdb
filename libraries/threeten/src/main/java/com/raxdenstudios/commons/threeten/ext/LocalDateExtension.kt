package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun LocalDate.toMilliseconds(): Long =
  atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDate.toSeconds(): Long =
  atStartOfDay().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000

fun LocalDate.isToday(): Boolean = this == LocalDate.now()

fun LocalDate.simpleFormat() = format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

fun LocalDate.format(pattern: String = "dd MMM yyyy") = format(DateTimeFormatter.ofPattern(pattern))
