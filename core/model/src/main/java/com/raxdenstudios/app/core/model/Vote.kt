package com.raxdenstudios.app.core.model

data class Vote(
    val average: Float,
    val count: Int,
) {

    companion object {

        val mock = Vote(
            average = 0.0f,
            count = 0,
        )
    }
}
