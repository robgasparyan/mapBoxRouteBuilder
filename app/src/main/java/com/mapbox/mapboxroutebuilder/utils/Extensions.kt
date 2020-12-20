package com.mapbox.mapboxroutebuilder.utils

import androidx.lifecycle.LiveData
import kotlin.random.Random

typealias Callback = () -> Unit

fun <T> List<T>.getRandomItemFromList(): T {
    return this[Random.nextInt(this.size - 1)]
}

fun <T> LiveData<T>.observeOnce(observer: androidx.lifecycle.Observer<T>) {
    observeForever(object : androidx.lifecycle.Observer<T> {
        override fun onChanged(t: T?) {
            observer.onChanged(t)
            removeObserver(this)
        }
    })
}

fun safeCall(run: Callback, onFail: Callback) {
    return try {
        run()
    } catch (ignore: Throwable) {
        onFail()
    }
}

fun safeCall(run: Callback) {
    try {
        run()
    } catch (ignore: Exception) {

    }
}
