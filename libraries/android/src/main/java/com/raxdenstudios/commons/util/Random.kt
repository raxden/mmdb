package com.raxdenstudios.commons.util

private val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

object Random {

  fun generateStringWithSize(size: Int) = (1..size)
    .map { kotlin.random.Random.nextInt(0, charPool.size) }
    .map(charPool::get)
    .joinToString("")
}
