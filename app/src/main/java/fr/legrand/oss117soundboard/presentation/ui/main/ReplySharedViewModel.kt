package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class ReplySharedViewModel @Inject constructor(
) : ViewModel() {

    val onSearchRequested = MutableLiveData<String>()

    fun requestSearch(search: String) {
        onSearchRequested.postValue(search)
    }

    companion object {
        const val NO_SEARCH = ""
    }
}
