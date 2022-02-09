package com.ivyclub.contact.service.password_timer

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.ivyclub.data.ContactPreference
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay

@HiltWorker
class PasswordTimerWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val preferences: ContactPreference) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        val timer = 300 - 1

        repeat(300) {
            preferences.setPasswordTimer(timer - it)
            delay(1000)
        }

        preferences.setPasswordTimer(-1)
        preferences.setPasswordTryCount(0)
        preferences.stopObservePasswordTimer()

        Result.success()
    }
}