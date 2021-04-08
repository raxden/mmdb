package com.raxdenstudios.commons.threeten.ext

import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter

fun String.toLocalDate(vararg patterns: String): LocalDate? {
  patterns.forEach { format ->
    val dateTimeFormatter = DateTimeFormatter.ofPattern(format)
    toLocalDate(dateTimeFormatter)?.also { localDate -> return localDate }
  }
  return null
}

fun String.toLocalDate(vararg formatters: DateTimeFormatter): LocalDate? {
  formatters.forEach { dateTimeFormatter ->
    try {
      LocalDate.parse(this, dateTimeFormatter)?.also { localDate -> return localDate }
    } catch (e: Exception) {
    }
  }
  return null
}
