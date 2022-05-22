package com.ivyclub.contact.util

import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

fun <T> Flow<T>.throttleFist(windowDuration: Long): Flow<T> = flow {
    var windowStartTime = System.currentTimeMillis()
    var emitted = false
    collect { value ->
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - windowStartTime
        if (diff >= windowDuration) {
            windowStartTime += diff / windowDuration * windowDuration
            emitted = false
        }
        if (!emitted) {
            emit(value)
            emitted = true
        }
    }
}

fun collectDataWhenStarted(lifecycleOwner: LifecycleOwner, todoFunction: suspend () -> Unit) {
    lifecycleOwner.lifecycleScope.launch {
        lifecycleOwner.repeatOnLifecycle(STARTED) {
            todoFunction()
        }
    }
}