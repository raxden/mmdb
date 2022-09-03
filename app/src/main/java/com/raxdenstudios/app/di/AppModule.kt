package com.raxdenstudios.app.di

import android.content.Context
import com.raxdenstudios.commons.ActivityHolder
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.provider.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun stringProvider(@ApplicationContext context: Context): StringProvider =
        StringProvider(context)

    @Provides
    fun dispatcherFacade(): DispatcherFacade = object : DispatcherFacade {
        override fun io() = Dispatchers.IO
        override fun default() = Dispatchers.Default
    }

    @Provides
    fun activityHolder(): ActivityHolder = ActivityHolder()

    @Provides
    fun providePaginationConfig(): Pagination.Config =
        Pagination.Config.default.copy(
            initialPage = Page(1),
            pageSize = PageSize(20),
            prefetchDistance = 4
        )
}
