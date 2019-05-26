package fr.legrand.oss117soundboard.presentation.ui.reply

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.ui.main.item.MovieCharacterViewData
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.utils.SingleLiveEvent
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */


class ReplyListViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val disposable = CompositeDisposable()

    val replyListLiveData = MutableLiveData<List<ReplyViewData>>()
    val replyFavoriteUpdated = SingleLiveEvent<Boolean>()

    private val characterFilters = mutableListOf<MovieCharacterViewData>()
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
                val result = applyFilter(it.map { ReplyViewData(it) })
                replyListLiveData.postValue(result)
            },
            onError = {

            }
        ))
    }

    private fun applyFilter(replies: List<ReplyViewData>): List<ReplyViewData> {
        if (characterFilters.isEmpty()) return replies
        val filterValues = characterFilters.map { it.getValue() }
        return replies.filter {
            var filtered = false
            it.getCharactersViewData().forEach {
                filtered = filtered || it.getValue() in filterValues
            }
            filtered
        }
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

    fun updateCharacterFilter(filters: List<MovieCharacterViewData>) {
        characterFilters.clear()
        characterFilters.addAll(filters)
    }
}