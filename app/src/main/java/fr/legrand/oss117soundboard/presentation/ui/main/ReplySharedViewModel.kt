package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.ui.main.item.FilterViewData
import fr.legrand.oss117soundboard.presentation.ui.main.item.MovieCharacterViewData
import fr.legrand.oss117soundboard.presentation.ui.reply.item.MovieViewData
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
    val onReplyListenFinished = MutableLiveData<Boolean>()
    val onListenRequested = SingleLiveEvent<Boolean>()

    val availableFilters = MutableLiveData<List<FilterViewData>>()
    val onCharacterFilterUpdated = MutableLiveData<List<MovieCharacterViewData>>()
    val onMovieFilterUpdated = MutableLiveData<List<MovieViewData>>()

    val characterFilters = mutableListOf<MovieCharacterViewData>()
    val movieFilters = mutableListOf<MovieViewData>()

    init {
        requestSearch(NO_SEARCH)
        initFilters()
        getCharacterFilterData()
        getMovieFilterData()
    }

    fun requestSearch(search: String) {
        onSearchRequested.postValue(search)
    }

    fun listenToReply(replyId: Int) {
        onListenRequested.postValue(true)
        contentRepository.playSoundMedia(replyId).subscribeOn(Schedulers.io())
                .subscribeBy(onError = { Timber.e(it) }, onComplete = { isPlayerRunning() })
    }

    fun listenToRandomReply() {
        onListenRequested.postValue(true)
        contentRepository.listenToRandomReply().subscribeOn(Schedulers.io())
                .subscribeBy(onComplete = { isPlayerRunning() }, onError = { Timber.e(it) })
    }


    fun selectCharacterFilters(selected: IntArray) {
        val filter = characterFilters.filterIndexed { index, character ->
            val result = index in selected
            character.selected = result
            result
        }
        onCharacterFilterUpdated.postValue(filter)
    }

    fun selectMovieFilters(selected: IntArray) {
        val filter = movieFilters.filterIndexed { index, character ->
            val result = index in selected
            character.selected = result
            result
        }
        onMovieFilterUpdated.postValue(filter)
    }

    private fun getCharacterFilterData() {
        contentRepository.getAllCharacters().subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = {
                    characterFilters.clear()
                    characterFilters.addAll(it.map { MovieCharacterViewData(it) })
                }, onError = {
                    //Nothing to do
                })
    }

    private fun getMovieFilterData() {
        contentRepository.getAllMovies().subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = {
                    movieFilters.clear()
                    movieFilters.addAll(it.map { MovieViewData(it) })
                }, onError = {
                    //Nothing to do
                })
    }

    private fun initFilters() {
        contentRepository.getAllFilters().subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = {
                    availableFilters.postValue(it.map { FilterViewData(it) })
                }, onError = {
                    //Nothing to do
                })
    }


    private fun isPlayerRunning() {
        contentRepository.isPlayerRunning().subscribeOn(Schedulers.io())
                .subscribeBy(onSuccess = {
                    if (!it) {
                        onReplyListenFinished.postValue(true)
                    }
                }, onError = {
                    //Nothing to do
                })
    }

    fun resetFilters() {
        //Called directly from Activity, we can use the setValue method on LiveData
        characterFilters.forEach { it.selected = false }
        onCharacterFilterUpdated.value = characterFilters
        movieFilters.forEach { it.selected = false }
        onMovieFilterUpdated.value = movieFilters
    }

    companion object {
        const val NO_SEARCH = ""
    }
}
