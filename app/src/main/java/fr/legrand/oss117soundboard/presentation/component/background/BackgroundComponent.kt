package fr.legrand.oss117soundboard.presentation.component.background

import io.reactivex.Observable


interface BackgroundComponent {
    fun startBackgroundListenService()
    fun stopBackgroundListenService()
    fun onForeground()
    fun onBackground()
    fun listenToAppState(): Observable<AppState>
}