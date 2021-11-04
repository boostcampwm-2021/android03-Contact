package com.ivyclub.data.repository

import androidx.room.*
import com.ivyclub.data.RoomConverters
import com.ivyclub.data.model.PersonData

@Database(entities = [PersonData::class],version = 1)
@TypeConverters(RoomConverters::class)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun personDataDAO(): ContactDAO
}