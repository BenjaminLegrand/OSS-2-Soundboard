package fr.legrand.oss117soundboard.presentation.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

fun <T> LiveData<T>.observeSafe(owner: LifecycleOwner, observer: (T) -> Unit) {
    this.observe(owner, Observer<T> { t ->
        if (t != null) {
            observer(t)
        }
    })
}