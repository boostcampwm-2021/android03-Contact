package com.ivyclub.contact.service

import android.content.Intent
import android.widget.RemoteViewsService
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactRemoteViewsService : RemoteViewsService() {
    @Inject
    lateinit var repository: ContactRepository

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return ContactRemoteViewsFactory(this.applicationContext, repository)
    }
}