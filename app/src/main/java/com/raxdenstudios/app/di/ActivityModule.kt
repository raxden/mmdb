package com.raxdenstudios.app.di

import android.app.Activity
import androidx.activity.ComponentActivity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
class ActivityModule {

    @Provides
    fun provideComponentActivity(activity: Activity): ComponentActivity {
        try {
            return activity as ComponentActivity
        } catch (e: ClassCastException) {
            throw IllegalStateException("Expected activity to be a FragmentActivity:  $activity", e)
        }
    }
}
