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
    fun provideContactDataBase(@ApplicationContext appContext: Context): ContactDatabase {
        return Room.databaseBuilder(
            appContext,
            ContactDatabase::class.java,
            "contact.db"
        ).addMigrations(ContactDatabase.MIGRATION_1_2).build()
    }

    @Provides
    fun provideContactDAO(contactDatabase: ContactDatabase): ContactDAO {
        return contactDatabase.contactDAO()
    }
}