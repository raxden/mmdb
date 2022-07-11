package com.raxdenstudios.app.network

import com.raxdenstudios.app.network.model.APIVersion
import javax.inject.Inject

class APIDataProviderFactory @Inject constructor() {

  fun create(version: APIVersion): APIDataProvider =
    when (version) {
      APIVersion.V3 -> APIDataV3Provider()
      APIVersion.V4 -> APIDataV4Provider()
    }
}
