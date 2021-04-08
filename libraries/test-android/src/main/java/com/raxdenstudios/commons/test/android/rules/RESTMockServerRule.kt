package com.raxdenstudios.commons.test.android.rules

import io.appflate.restmock.RESTMockServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class RESTMockServerRule : TestWatcher() {

  override fun finished(description: Description?) {
    super.finished(description)
    RESTMockServer.reset()
  }
}
