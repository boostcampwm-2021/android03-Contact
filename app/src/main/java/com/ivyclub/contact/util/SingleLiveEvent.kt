package com.ivyclub.contact.util

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import java.util.concurrent.atomic.AtomicBoolean

class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val isPending = AtomicBoolean(false)

    @MainThread
    override fun setValue(value: T?) {
        isPending.set(true)
        super.setValue(value)
    }

    override fun postValue(value: T?) {
        isPending.set(true)
        super.postValue(value)
    }

    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        super.observe(owner, {
            if (isPending.compareAndSet(true, false)) {
                observer.onChanged(it)
            }
        })
    }

    fun call() {
        postValue(null)
    }
}