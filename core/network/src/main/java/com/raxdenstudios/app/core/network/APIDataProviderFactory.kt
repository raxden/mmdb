package com.raxdenstudios.app.core.network

import com.raxdenstudios.app.core.network.model.APIVersion
import javax.inject.Inject

class APIDataProviderFactory @Inject constructor() {

    fun create(version: APIVersion): APIDataProvider =
        when (version) {
            APIVersion.V3 -> APIDataV3Provider()
            APIVersion.V4 -> APIDataV4Provider()
        }
}
