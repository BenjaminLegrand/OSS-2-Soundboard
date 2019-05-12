package fr.legrand.oss117soundboard.presentation.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.data.values.SortType
import fr.legrand.oss117soundboard.data.values.PlayerState
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.ui.settings.item.SortViewData
import io.reactivex.disposables.CompositeDisposable
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
    application: Application
) : AndroidViewModel(application) {

    private val disposable = CompositeDisposable()
    val replySort = MutableLiveData<SortViewData>()
    val multiListenEnabled = MutableLiveData<Boolean>()
    val mostListenedReply = MutableLiveData<ReplyViewData>()
    val totalReplyTime = MutableLiveData<Triple<Long, Long, Long>>()

    init {
        checkMultiListenEnabled()
        getReplySort()
        getMostListenedReply()
        getTotalReplyTime()
        listenToPlayerState()
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    private fun listenToPlayerState() {
        disposable.add(contentRepository.listenToPlayerState().subscribeOn(Schedulers.io()).subscribeBy(
            onNext = {
                when (it) {
                    PlayerState.STOP -> updateAllReplyData()
                }
            }, onError = { Timber.e(it) })
        )
    }

    fun updateMultiListenParameter(multiListen: Boolean) {
        contentRepository.updateMultiListenParameter(multiListen).subscribeOn(Schedulers.io()).subscribeBy(
            onComplete = {
                multiListenEnabled.postValue(multiListen)
            }, onError = { Timber.e(it) })
    }

    fun listenToRandomReply() {
        contentRepository.listenToRandomReply().subscribeOn(Schedulers.io())
            .subscribeBy(onComplete = { }, onError = { Timber.e(it) })
    }

    fun updateReplySort(newSort: SortType) {
        contentRepository.updateReplySort(newSort).subscribeOn(Schedulers.io())
            .subscribeBy(
                onComplete = { replySort.postValue(SortViewData(getApplication(), newSort)) },
                onError = { Timber.e(it) })
    }


    private fun checkMultiListenEnabled() {
        contentRepository.multiListenEnabled().subscribeOn(Schedulers.io())
            .subscribeBy(onNext = { multiListenEnabled.postValue(it) }, onError = { Timber.e(it) })
    }

    private fun getReplySort() {
        contentRepository.getReplySort().subscribeOn(Schedulers.io())
            .subscribeBy(onSuccess = {
                replySort.postValue(SortViewData(getApplication(), it))
            }, onError = { Timber.e(it) })
    }

    private fun getMostListenedReply() {
        contentRepository.getMostListenedReply().subscribeOn(Schedulers.io()).subscribeBy(
            onNext = {
                mostListenedReply.postValue(ReplyViewData(it))
            },
            onError = {
                Timber.e(it)
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

    private fun updateAllReplyData() {
        getMostListenedReply()
        getTotalReplyTime()
    }
}
