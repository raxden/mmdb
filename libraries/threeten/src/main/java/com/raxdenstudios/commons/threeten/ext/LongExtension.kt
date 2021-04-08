package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.Instant
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId

fun Long.toLocalDate(): LocalDate =
  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun Long.toLocalDateTime(): LocalDateTime =
  Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDateTime()
