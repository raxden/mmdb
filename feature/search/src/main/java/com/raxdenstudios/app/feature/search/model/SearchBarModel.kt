package com.raxdenstudios.app.feature.search.model

sealed class SearchBarModel {

    object Idle : SearchBarModel()
    object Focused : SearchBarModel()
    data class Searching(val query: String) : SearchBarModel()
    data class WithResults(val query: String) : SearchBarModel()
    data class WithoutResults(val query: String) : SearchBarModel()
}
