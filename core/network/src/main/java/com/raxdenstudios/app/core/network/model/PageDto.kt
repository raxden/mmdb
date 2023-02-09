package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

@Suppress("ConstructorParameterNaming")
data class PageDto<T>(
    @Expose val page: Int = 0,
    @Expose val total_pages: Int = 0,
    @Expose val total_results: Int = 0,
    @Expose val results: List<T>,
) {

    companion object {

        fun <T> empty(): PageDto<T> = PageDto(
            page = 0,
            total_pages = 0,
            total_results = 0,
            results = emptyList()
        )
    }
}
