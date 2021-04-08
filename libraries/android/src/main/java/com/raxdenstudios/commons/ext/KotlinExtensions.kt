package com.raxdenstudios.commons.ext

inline val <reified T> T?.exhaustive: T?
  get() = this
