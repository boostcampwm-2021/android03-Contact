package com.ivyclub.contact.receivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.ivyclub.contact.R
import com.ivyclub.data.ContactRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LanguageChangedReceiver : BroadcastReceiver() {

    @Inject
    lateinit var repository: ContactRepository

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null || context == null) return

        if (intent.action == Intent.ACTION_LOCALE_CHANGED) {
            val translatedFriendGroupName = context.getString(R.string.bnv_friend)

            CoroutineScope(Dispatchers.IO).launch {
                repository.updateFriendGroupName(translatedFriendGroupName)
            }
        }
    }
}