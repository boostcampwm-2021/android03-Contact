package com.ivyclub.data

import android.content.Context
import androidx.room.Room
import com.ivyclub.data.repository.ContactDAO
import com.ivyclub.data.repository.ContactDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun providePersonDataBase(@ApplicationContext appContext: Context): ContactDatabase {
        return Room.databaseBuilder(
            appContext,
            ContactDatabase::class.java,
            "person.db"
        ).build()
    }

    @Provides
    fun providePersonDao(contactDatabase: ContactDatabase): ContactDAO {
        return contactDatabase.personDataDAO()
    }
}