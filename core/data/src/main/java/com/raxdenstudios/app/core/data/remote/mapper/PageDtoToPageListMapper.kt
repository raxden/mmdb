package com.raxdenstudios.app.core.data.remote.mapper

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.commons.DataMapper
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import javax.inject.Inject

class PageDtoToPageListMapper @Inject constructor(
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper
) : DataMapper<PageDto<out MediaDto>, PageList<Media>>() {

    override fun transform(source: PageDto<out MediaDto>): PageList<Media> =
        PageList(
            items = mediaDtoToDomainMapper.transform(source.results),
            page = Page(source.page)
        )
}
