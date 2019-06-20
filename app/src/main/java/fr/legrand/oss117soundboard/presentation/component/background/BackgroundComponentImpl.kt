package fr.legrand.oss117soundboard.presentation.component.background

import android.content.Context
import android.content.Intent
import fr.legrand.oss117soundboard.presentation.service.BackgroundListenService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BackgroundComponentImpl @Inject constructor(private val context: Context) :
    BackgroundComponent {

    private val appStateSubject = PublishSubject.create<AppState>()

    override fun startBackgroundListenService() {
        context.startService(Intent(context, BackgroundListenService::class.java))
    }

    override fun stopBackgroundListenService() {
        context.stopService(Intent(context, BackgroundListenService::class.java))
    }

    override fun onForeground() {
        appStateSubject.onNext(AppState.FOREGROUND)
    }

    override fun onBackground() {
        appStateSubject.onNext(AppState.BACKGROUND)
    }

    override fun listenToAppState(): Observable<AppState> = appStateSubject

}