package com.ivyclub.contact.util

import android.content.Intent
import android.widget.RemoteViewsService
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyRemoteViewsService : RemoteViewsService() {
    @Inject
    lateinit var repository: ContactRepository

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return MyRemoteViewsFactory(this.applicationContext, repository)
    }
}