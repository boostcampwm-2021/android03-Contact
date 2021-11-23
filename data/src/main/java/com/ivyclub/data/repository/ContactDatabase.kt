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
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDAO(): ContactDAO

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """CREATE TABLE `tmp_friendData` (
                        `phoneNumber` TEXT NOT NULL, 
                        `name` TEXT NOT NULL, 
                        `birthday` TEXT NOT NULL, 
                        `groupId` INTEGER NOT NULL, 
                        `planList` TEXT NOT NULL, 
                        `isFavorite` INTEGER NOT NULL, 
                        `extraInfo` TEXT NOT NULL, 
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
                        )"""
                        .trimMargin()
                )

                database.execSQL("DROP TABLE FriendData")
                database.execSQL("ALTER TABLE tmp_friendData RENAME TO FriendData")

                database.execSQL(
                    """CREATE TABLE `tmp_groupData` (
                        `name` TEXT NOT NULL,
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL
                        )"""
                        .trimMargin()
                )

                database.execSQL("DROP TABLE GroupData")
                database.execSQL("ALTER TABLE tmp_groupData RENAME TO GroupData")
                database.execSQL("DELETE FROM PlanData")
            }
        }
    }
}