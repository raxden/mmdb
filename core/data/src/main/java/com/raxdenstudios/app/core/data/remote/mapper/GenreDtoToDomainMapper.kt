package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.model.Genre
import com.raxdenstudios.app.core.model.GenreId
import com.raxdenstudios.app.core.network.model.GenreDto
import javax.inject.Inject

class GenreDtoToDomainMapper @Inject constructor() {

    fun transform(source: Int): Genre = Genre(
        id = GenreId(source.toLong()),
        name = ""
    )

    fun transform(source: GenreDto): Genre = Genre(
        id = GenreId(source.id.toLong()),
        name = source.name,
    )
}
