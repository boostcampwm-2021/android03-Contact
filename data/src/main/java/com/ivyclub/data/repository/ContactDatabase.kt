package com.ivyclub.data.repository

import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.ivyclub.data.RoomConverters
import com.ivyclub.data.model.FriendData
import com.ivyclub.data.model.PlanData
import com.ivyclub.data.model.GroupData

@Database(entities = [FriendData::class, GroupData::class, PlanData::class], version = 2)
@TypeConverters(RoomConverters::class)
abstract class ContactDatabase: RoomDatabase() {
    abstract fun contactDAO(): ContactDAO
}