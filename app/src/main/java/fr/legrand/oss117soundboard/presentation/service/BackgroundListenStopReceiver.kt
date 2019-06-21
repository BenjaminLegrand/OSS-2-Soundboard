package fr.legrand.oss117soundboard.presentation.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.android.AndroidInjection
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.component.background.BackgroundComponent
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

class BackgroundListenStopReceiver : BroadcastReceiver() {

    @Inject
    lateinit var contentRepository: ContentRepository
    @Inject
    lateinit var backgroundComponent: BackgroundComponent

    override fun onReceive(context: Context?, intent: Intent?) {
        AndroidInjection.inject(this, context)
        contentRepository.releaseRunningPlayers().subscribeBy(onComplete = {}, onError = {})
        backgroundComponent.stopBackgroundListenService()
    }

}