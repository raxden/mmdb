package com.raxdenstudios.app.core.network.model

import com.google.gson.annotations.Expose

data class PageDto<T : Any>(
    @Expose val page: Int,
    @Expose val total_pages: Int,
    @Expose val total_results: Int,
    @Expose val results: List<T>,
) {

    companion object {

        val empty = PageDto(
            page = 0,
            total_pages = 0,
            total_results = 0,
            results = emptyList()
        )
    }
}
