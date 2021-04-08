package com.raxdenstudios.commons.util

abstract class DataMapper<E, T> {

  abstract fun transform(source: E): T

  fun transform(source: List<E>): List<T> =
    source.map { sourceData -> transform(sourceData) }
}
