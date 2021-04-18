package com.raxdenstudios.commons.android.test

import androidx.test.espresso.Espresso.pressBack

open class TestPage {

  companion object {
    inline fun <reified T : TestPage> on(): T {
      return TestPage().on()
    }
  }

  inline fun <reified T : TestPage> on(): T {
    val page = initPage<T>()
    page.verify()
    return page
  }

  inline fun <reified T : TestPage> initPage() =
    if (T::class.constructors.isEmpty()) T::class.objectInstance!!
    else T::class.constructors.first().call()

  open fun verify(): TestPage {
    // Each subpage should have its default assurances here
    return this
  }

  fun back(): TestPage {
    pressBack()
    return this
  }
}
