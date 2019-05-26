package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.utils.SingleLiveEvent
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class ReplySharedViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    val onSearchRequested = MutableLiveData<String>()
    val onReplyListened = SingleLiveEvent<Boolean>()

    init {
        requestSearch(NO_SEARCH)
    }

    fun requestSearch(search: String) {
        onSearchRequested.postValue(search)
    }

    fun listenToReply(replyId: Int) {
        contentRepository.playSoundMedia(replyId).subscribeOn(Schedulers.io())
            .subscribeBy(onError = { Timber.e(it) }, onComplete = { onReplyListened.postValue(true) })
    }

    fun listenToRandomReply() {
        contentRepository.listenToRandomReply().subscribeOn(Schedulers.io())
            .subscribeBy(onComplete = { onReplyListened.postValue(true) }, onError = { Timber.e(it) })
    }

    companion object {
        const val NO_SEARCH = ""
    }
}
