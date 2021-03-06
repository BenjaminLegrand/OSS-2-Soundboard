package fr.legrand.oss117soundboard.presentation.ui.reply

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.ui.main.item.MovieCharacterViewData
import fr.legrand.oss117soundboard.presentation.ui.reply.item.MovieViewData
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.utils.SingleLiveEvent
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
) : ViewModel() {

    private val disposable = CompositeDisposable()

    val replyListLiveData = MutableLiveData<List<ReplyViewData>>()
    val replyFavoriteUpdated = SingleLiveEvent<Boolean>()
    val resultsIndicator = SingleLiveEvent<Int>()
    val replyShareData = SingleLiveEvent<Pair<Uri, String>>()

    private val characterFilters = mutableListOf<MovieCharacterViewData>()
    private val movieFilters = mutableListOf<MovieViewData>()
    private var currentSearch = ReplySharedViewModel.NO_SEARCH

    override fun onCleared() {
        disposable.clear()
    }

    fun updateSearch(search: String) {
        currentSearch = search
    }

    fun getAllReply(favorite: Boolean) {
        disposable.clear()
        val obs = if (currentSearch == ReplySharedViewModel.NO_SEARCH) {
            contentRepository.getAllReply(favorite)
        } else {
            contentRepository.getReplyWithSearch(currentSearch, favorite)
        }
        disposable.add(obs.subscribeOn(Schedulers.io()).subscribeBy(
                onNext = {
                    val result = applyFilters(it.map { ReplyViewData(it) })
                    replyListLiveData.postValue(result)
                },
                onError = {
                    Timber.e(it)
                }
        ))
    }

    fun getAllReplyNegated(favorite: Boolean) {
        disposable.clear()
        val obs = if (currentSearch == ReplySharedViewModel.NO_SEARCH) {
            contentRepository.getAllReply(!favorite)
        } else {
            contentRepository.getReplyWithSearch(currentSearch, !favorite)
        }
        disposable.add(obs.subscribeOn(Schedulers.io()).subscribeBy(
                onNext = {
                    val result = applyFilters(it.map { ReplyViewData(it) })
                    resultsIndicator.postValue(result.size)
                },
                onError = {
                    Timber.e(it)
                }
        ))
    }

    fun generateReplyShareData(replyId: Int) {
        disposable.add(contentRepository.generateShareData(replyId).subscribeOn(Schedulers.io()).subscribeBy(
                onSuccess = {
                    replyShareData.postValue(it)
                },
                onError = {
                    Timber.e(it)
                }
        ))
    }

    fun setReplyAsRingtone(replyId: Int) {
        disposable.add(contentRepository.setReplyAsRingtone(replyId).subscribeOn(Schedulers.io()).subscribeBy(
                onComplete = {
                },
                onError = {
                    Timber.e(it)
                }
        ))
    }

    private fun applyFilters(replies: List<ReplyViewData>): List<ReplyViewData> {
        val characterFiltered = applyCharacterFilters(replies)
        return applyMovieFilters(characterFiltered)
    }

    private fun applyCharacterFilters(replies: List<ReplyViewData>): List<ReplyViewData> {
        if (characterFilters.isEmpty()) return replies
        val characterFilterValues = characterFilters.map { it.getValue() }
        return replies.filter {
            var filteredCharacter = false
            it.getCharactersViewData().forEach {
                filteredCharacter = filteredCharacter || it.getValue() in characterFilterValues
            }
            filteredCharacter
        }
    }

    private fun applyMovieFilters(replies: List<ReplyViewData>): List<ReplyViewData> {
        if (movieFilters.isEmpty()) return replies
        val movieFilterValues = movieFilters.map { it.getValue() }
        return replies.filter {
            it.getMovieViewData().getValue() in movieFilterValues
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

    fun updateMovieFilter(filters: List<MovieViewData>) {
        movieFilters.clear()
        movieFilters.addAll(filters)
    }

}