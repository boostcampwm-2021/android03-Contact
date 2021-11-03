package com.ivyclub.data

import com.ivyclub.data.model.PersonData
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

interface ContactRepository {
    fun loadPeople()
    fun savePeople(personData: PersonData)
}