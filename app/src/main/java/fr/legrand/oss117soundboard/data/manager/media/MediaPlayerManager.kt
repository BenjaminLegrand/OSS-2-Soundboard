package fr.legrand.oss117soundboard.data.manager.media

import io.reactivex.Completable

/**
 * Created by Benjamin on 30/09/2017.
 */

interface MediaPlayerManager {
    fun isPlayerCurrentlyRunning(): Boolean

    fun releaseAllRunningPlayer()

    fun playSoundMedia(mediaId: Int, multiListen: Boolean): Completable
}
