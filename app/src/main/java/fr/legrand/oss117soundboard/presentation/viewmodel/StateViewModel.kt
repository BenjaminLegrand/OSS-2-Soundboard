package fr.legrand.oss117soundboard.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class StateViewModel<T> : ViewModel() {
    protected abstract val currentViewState: T

    val viewState = MutableLiveData<T>()

    protected inline fun <reified T> MutableLiveData<T>.update(block: T.() -> Unit) {
        this.postValue((currentViewState as T).apply(block))
    }
}
