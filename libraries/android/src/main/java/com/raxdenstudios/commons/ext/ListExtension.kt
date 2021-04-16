package com.raxdenstudios.commons.ext

fun <T> List<T>.replaceItem(newValue: T, condition: (T) -> Boolean): List<T> {
  return map { list ->
    if (condition(list)) newValue else list
  }
}

fun <T> List<T>.removeItem(condition: (T) -> Boolean): List<T> {
  return filterNot(condition)
}
