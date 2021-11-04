package com.ivyclub.data.repository

import com.ivyclub.data.ContactRepository
import com.ivyclub.data.model.PersonData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactRepositoryImpl @Inject constructor(
    private val contactDAO: ContactDAO
    ): ContactRepository{
    override fun loadPeople() {
        contactDAO.getAllPerson()
    }

    override fun savePeople(personData: PersonData) {
        contactDAO.insertPersonData(personData)
    }
}