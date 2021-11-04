package com.ivyclub.data.repository

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.ivyclub.data.model.PersonData

@Dao
interface ContactDAO {

    @Query("SELECT * FROM PersonData")
    fun getAllPerson(): List<PersonData>

    @Insert
    fun insertPersonData(personData: PersonData)
}