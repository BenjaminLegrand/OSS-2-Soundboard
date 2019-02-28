package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.presentation.utils.SingleLiveEvent
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class ReplySharedViewModel @Inject constructor() : ViewModel() {

    val onSearchRequested = MutableLiveData<String>()

    val onReplyListened = MutableLiveData<Boolean>()
    val onReplyFavoriteUpdated = MutableLiveData<Boolean>()
    val onSortUpdated = MutableLiveData<Boolean>()

    fun requestSearch(search: String) {
        onSearchRequested.postValue(search)
    }

    fun onReplyListened() {
        onReplyListened.postValue(true)
    }

    fun onReplyFavoriteUpdated() {
        onReplyFavoriteUpdated.postValue(true)
    }

    fun onSortUpdated() {
        onSortUpdated.postValue(true)
    }

    companion object {
        const val NO_SEARCH = ""
    }
}
