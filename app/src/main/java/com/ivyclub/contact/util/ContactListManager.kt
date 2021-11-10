package com.ivyclub.contact.util

import android.annotation.SuppressLint
import android.content.Context
import android.provider.ContactsContract
import com.ivyclub.contact.model.PhoneContactData
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ContactListManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    @SuppressLint("Range")
    fun getContact(): MutableList<PhoneContactData> {
        val contactList: MutableList<PhoneContactData> = mutableListOf()
        val contacts = context.applicationContext?.contentResolver?.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        ) ?: return mutableListOf()
        while (contacts.moveToNext()) {
            val name =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
            val number =
                contacts.getString(contacts.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
            val obj = PhoneContactData(name, number)
            contactList.add(obj)
        }
        contacts.close()
        return contactList
    }
}