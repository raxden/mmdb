package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.format.DateTimeFormatter

fun LocalDateTime.toSeconds(): Long =
  atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 1000

fun LocalDateTime.toMilliseconds(): Long = atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()

fun LocalDateTime.simpleFormat() = format(DateTimeFormatter.ofPattern("dd MMM yyyy"))

fun LocalDateTime.format(pattern: String = "dd MMM yyyy") =
  format(DateTimeFormatter.ofPattern(pattern))
