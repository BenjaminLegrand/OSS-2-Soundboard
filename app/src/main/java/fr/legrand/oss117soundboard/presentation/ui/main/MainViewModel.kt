package fr.legrand.oss117soundboard.presentation.ui.main

import androidx.lifecycle.ViewModel
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class MainViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {


    init {
        initAllReply()
    }

    private fun initAllReply() {
        contentRepository.initAllReply().subscribeOn(Schedulers.io())
            .subscribeBy(onComplete = {
            }, onError = {
                Timber.e(it)
            })
    }
}
