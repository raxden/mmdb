package com.raxdenstudios.app.di

import android.content.Context
import com.raxdenstudios.commons.ActivityHolder
import com.raxdenstudios.commons.coroutines.DispatcherProvider
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.android.provider.StringProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun stringProvider(
        @ApplicationContext context: Context
    ): StringProvider = StringProvider(context)

    @Provides
    fun dispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
    }

    @Provides
    fun activityHolder(): ActivityHolder = ActivityHolder()

    @Provides
    fun providePaginationConfig(): Pagination.Config = paginationConfig

    companion object {

        private val paginationConfig = Pagination.Config.default.copy(
            initialPage = Page(1),
            pageSize = PageSize(20),
            prefetchDistance = 4
        )
    }
}
