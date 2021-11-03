package com.ivyclub.data.repository

import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PersonData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val personDao: PersonDao
    ): ContactRepository{
    override fun loadPeople() {
        personDao.getAllPerson()
    }

    override fun savePeople(personData: PersonData) {
        personDao.insertPersonData(personData)
    }
}