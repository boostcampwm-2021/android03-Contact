package com.ivyclub.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ivyclub.data.model.PersonData

@Dao
interface PersonDao {

    @Query("SELECT * FROM persondata")
    fun getAllPerson(): List<PersonData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPersonData(personData: PersonData)
}