package com.raxdenstudios.commons.ext

fun <T> List<T>.replaceItem(newValue: T, block: (T) -> Boolean): List<T> {
  return map { list ->
    if (block(list)) newValue else list
  }
}

fun <T> List<T>.removeItem(block: (T) -> Boolean): List<T> {
  return filterNot(block)
}
