package fr.legrand.oss117soundboard.data.manager.media

import fr.legrand.oss117soundboard.data.values.PlayerStatus
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by Benjamin on 30/09/2017.
 */

interface MediaPlayerManager {
    fun isPlayerCurrentlyRunning(): Boolean

    fun releaseAllRunningPlayer()

    fun playSoundMedia(mediaId: Int, multiListen: Boolean): Completable

    fun listenToMediaPlayerStatus(): Observable<PlayerStatus>

    fun getPlayerRunningCount(): Int
}
