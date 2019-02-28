package fr.legrand.oss117soundboard.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData

abstract class AndroidStateViewModel<T>(application: Application) : AndroidViewModel(application) {
    abstract val currentViewState: T

    val viewState = MutableLiveData<T>()

    protected inline fun <reified T> MutableLiveData<T>.update(block: T.() -> Unit) {
        this.postValue((currentViewState as T).apply(block))
    }
}
