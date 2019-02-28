package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.component.MediaPlayerComponent
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class MainViewModel @Inject constructor(
        private val contentRepository: ContentRepository,
        private val mediaPlayerComponent: MediaPlayerComponent
) : ViewModel() {

    val replyLoaded = MutableLiveData<Boolean>()

    init {
        initAllReply()
    }

    private fun initAllReply() {
        contentRepository.initAllReply().subscribeOn(Schedulers.io())
                .subscribeBy(onComplete = {
                    replyLoaded.postValue(true)
                }, onError = {
                    //Nothing to do
                })
    }

    fun releaseRunningPlayers() {
        mediaPlayerComponent.releaseAllRunningPlayer()
    }

    override fun onCleared() {
        releaseRunningPlayers()
    }
}
