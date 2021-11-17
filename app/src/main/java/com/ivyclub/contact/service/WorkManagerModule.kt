package com.ivyclub.contact.service

import android.content.Context
import androidx.work.WorkManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
class WorkManagerModule {
    @Provides
    fun provideWorkManager(@ApplicationContext context: Context) =
        WorkManager.getInstance(context)
}