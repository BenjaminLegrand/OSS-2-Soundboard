package fr.legrand.oss117soundboard.presentation.ui.reply

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.utils.SingleLiveEvent
import fr.legrand.oss117soundboard.presentation.viewmodel.StateViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */


class ReplyListViewModel @Inject constructor(
        private val contentRepository: ContentRepository
) : StateViewModel<ReplyListViewState>() {

    override val currentViewState = ReplyListViewState()

    private val disposable = CompositeDisposable()

    val replyListLiveData = MutableLiveData<List<ReplyViewData>>()
    val replyFavoriteUpdated = SingleLiveEvent<Boolean>()

    private var currentSearch = ReplySharedViewModel.NO_SEARCH

    override fun onCleared() {
        disposable.clear()
    }

    fun updateSearch(search: String) {
        currentSearch = search
    }

    fun getAllReply(favorite: Boolean) {
        val obs = if (currentSearch == ReplySharedViewModel.NO_SEARCH) {
            contentRepository.getAllReply(favorite)
        } else {
            contentRepository.getReplyWithSearch(currentSearch, favorite)
        }
        disposable.add(obs.subscribeOn(Schedulers.io()).subscribeBy(
                onNext = {
                    replyListLiveData.postValue(it.map { ReplyViewData(it) })
                    viewState.update {
                        displayingPlaceholder = it.isEmpty()
                    }

                },
                onError = {
                    viewState.update {
                        displayingPlaceholder = true
                    }
                }
        ))
    }

    fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean) {
        disposable.add(
                contentRepository.updateFavoriteReply(
                        replyId,
                        addToFavorite
                ).subscribeOn(Schedulers.io()).subscribe {
                    replyFavoriteUpdated.postValue(true)
                })
    }
}