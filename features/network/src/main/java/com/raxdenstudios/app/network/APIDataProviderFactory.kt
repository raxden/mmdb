package com.raxdenstudios.app.network

import com.raxdenstudios.app.network.model.APIVersion

internal class APIDataProviderFactory {

  companion object {
    fun create(version: APIVersion): APIDataProvider =
      when (version) {
        APIVersion.V3 -> APIDataV3Provider()
        APIVersion.V4 -> APIDataV4Provider()
      }
  }
}
