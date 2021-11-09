package com.ivyclub.data.repository

import androidx.room.*
import com.ivyclub.data.RoomConverters
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.GroupData

@Database(entities = [FriendData::class, GroupData::class],version = 1)
@TypeConverters(RoomConverters::class)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDAO(): ContactDAO
}