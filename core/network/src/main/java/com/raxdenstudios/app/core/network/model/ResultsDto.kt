package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

data class ResultsDto<T>(
    @Expose val results: List<T>,
)
