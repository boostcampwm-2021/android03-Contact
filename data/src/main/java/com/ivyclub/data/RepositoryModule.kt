package com.ivyclub.data

import com.ivyclub.data.repository.ContactRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module
interface RepositoryModule {
    @Binds
    fun provideContractRepositoryImpl(repository: ContactRepositoryImpl): ContactRepository
}