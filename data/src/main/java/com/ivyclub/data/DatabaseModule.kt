package com.ivyclub.data

import android.content.Context
import androidx.room.Room
import com.ivyclub.data.repository.PersonDao
import com.ivyclub.data.repository.PersonDataBase
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
    fun providePersonDataBase(@ApplicationContext appContext: Context): PersonDataBase {
        return Room.databaseBuilder(
            appContext,
            PersonDataBase::class.java,
            "person.db"
        ).build()
    }

    @Provides
    fun providePersonDao(personDatabase: PersonDataBase): PersonDao {
        return personDatabase.personDataDAO()
    }
}