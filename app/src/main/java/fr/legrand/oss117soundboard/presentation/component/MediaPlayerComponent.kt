package fr.legrand.oss117soundboard.presentation.component

import io.reactivex.Completable

/**
 * Created by Benjamin on 30/09/2017.
 */

interface MediaPlayerComponent {
    fun isPlayerCurrentlyRunning(): Boolean

    fun releaseAllRunningPlayer()

    fun playSoundMedia(mediaId: Int): Completable
}
