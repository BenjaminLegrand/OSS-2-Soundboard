package fr.legrand.oss117soundboard.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.MutableLiveData
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.data.values.SortValues
import fr.legrand.oss117soundboard.presentation.OSSApplication
import fr.legrand.oss117soundboard.presentation.component.MediaPlayerComponent
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.viewmodel.AndroidStateViewModel
import fr.legrand.oss117soundboard.presentation.viewmodel.StateViewModel
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Benjamin on 17/10/2017.
 */

private const val MS_TO_S: Long = 1000
private const val MS_TO_M = (60 * 1000).toLong()
private const val MS_TO_H = (60 * 60 * 1000).toLong()
private const val M_S_MODULO_VALUE: Long = 60

class SettingsViewModel @Inject constructor(
    private val contentRepository: ContentRepository,
    private val mediaPlayerComponent: MediaPlayerComponent,
    application: Application
) : AndroidStateViewModel<SettingsViewState>(application) {
    override val currentViewState = SettingsViewState()

    val replySort = MutableLiveData<String>()
    val multiListenEnabled = MutableLiveData<Boolean>()
    val mostListenedReply = MutableLiveData<ReplyViewData>()
    val totalReplyTime = MutableLiveData<Triple<Long, Long, Long>>()

    init {
        checkMultiListenEnabled()
        getReplySort()
        getMostListenedReply()
        getTotalReplyTime()
    }

    fun updateMultiListenParameter(multiListen: Boolean) {
        contentRepository.updateMultiListenParameter(multiListen).subscribeOn(Schedulers.io()).subscribeBy(
            onComplete = {
                mediaPlayerComponent.releaseAllRunningPlayer()
                multiListenEnabled.postValue(multiListen)
            }, onError = { it.printStackTrace() })
    }

    fun listenToRandomReply() {
        contentRepository.retrieveRandomReplyIdToListen().subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { listenToReply(it) }, onError = { it.printStackTrace() })
    }

    fun updateReplySort(newSort: String) {
        contentRepository.updateReplySort(newSort).subscribeOn(Schedulers.io())
            .subscribeBy(onComplete = { replySort.postValue(newSort) }, onError = { it.printStackTrace() })
    }

    fun updateAllReplyData() {
        getMostListenedReply()
        getTotalReplyTime()
    }

    private fun checkMultiListenEnabled() {
        contentRepository.multiListenEnabled().subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { multiListenEnabled.postValue(it) }, onError = { it.printStackTrace() })
    }

    private fun getReplySort() {
        contentRepository.getReplySort().subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = {
                val sortText = when(it){
                    SortValues.ALPHABETICAL_SORT -> getApplication<OSSApplication>().getString(R.string.alphabetical_order)
                    SortValues.MOVIE_SORT ->getApplication<OSSApplication>().getString(R.string.movie_order)
                    SortValues.RANDOM_SORT -> getApplication<OSSApplication>().getString(R.string.alphabetical_order)
                }
                replySort.postValue(sortText)
            }, onError = { Timber.e(it) })
    }

    private fun getMostListenedReply() {
        contentRepository.getMostListenedReply().subscribeOn(Schedulers.io()).subscribeBy(
            onNext = {
                if(it.listenCount > 0){
                    viewState.update { mostListenedReplyAvailable = true }
                    mostListenedReply.postValue(ReplyViewData(it))
                }else{
                    viewState.update { mostListenedReplyAvailable = false }
                }
            },
            onError = {
                viewState.update { mostListenedReplyAvailable = false }
            })
    }

    private fun getTotalReplyTime() {
        contentRepository.getTotalReplyTime().subscribeOn(Schedulers.io())
            .subscribeBy(onNext = {
                totalReplyTime.postValue(
                    Triple(
                        it / MS_TO_H,
                        it / MS_TO_M % M_S_MODULO_VALUE,
                        it / MS_TO_S % M_S_MODULO_VALUE
                    )
                )
            },
                onError = { Timber.e(it) })
    }

    private fun listenToReply(replyId: Int) {
        contentRepository.incrementReplyListenCount(replyId).subscribeBy(onError = {}, onComplete = {})
        mediaPlayerComponent.playSoundMedia(replyId).subscribeOn(Schedulers.io())
            .subscribeBy(onError = { Timber.e(it) }, onComplete = {})
    }

}
