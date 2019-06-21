package fr.legrand.oss117soundboard.data.manager.media

import android.content.Context
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import fr.legrand.oss117soundboard.data.entity.RunningPlayer
import fr.legrand.oss117soundboard.data.values.PlayerStatus
import fr.legrand.oss117soundboard.presentation.utils.onStopListener
import fr.legrand.oss117soundboard.presentation.utils.startMedia
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Benjamin on 30/09/2017.
 */

private const val MULTI_LISTEN_NUMBER_LIMIT = 8

@Singleton
class MediaPlayerManagerImpl @Inject constructor(
    private val context: Context
) : MediaPlayerManager {


    private val runningMediaPlayerList = mutableListOf<RunningPlayer>()

    private val mediaPlayingSubject = PublishSubject.create<PlayerStatus>()

    override fun playSoundMedia(mediaId: Int, multiListen: Boolean): Completable {
        return Completable.create { emitter ->
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(null)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

            runningMediaPlayerList.add(RunningPlayer(simpleExoPlayer, mediaId))

            if (!multiListen && runningMediaPlayerList.isNotEmpty()
                || runningMediaPlayerList.size >= MULTI_LISTEN_NUMBER_LIMIT
            ) {
                stopRunningPlayer(runningMediaPlayerList[0])
            }

            simpleExoPlayer.onStopListener(onStop = {
                releaseRunningPlayerById(mediaId)
                updatePlayerStatusOnStop()
                emitter.onComplete()
            }, onError = {
                updatePlayerStatusOnStop()
                emitter.onError(it)
            })
            simpleExoPlayer.startMedia(context, mediaId) {
                emitter.onError(it)
            }

        }.observeOn(Schedulers.io())
    }

    override fun getPlayerRunningCount(): Int = runningMediaPlayerList.size

    private fun updatePlayerStatusOnStop() {
        if (runningMediaPlayerList.isEmpty()) {
            mediaPlayingSubject.onNext(PlayerStatus.ENDED)
        } else {
            mediaPlayingSubject.onNext(PlayerStatus.SINGLE_REPLY_ENDED)
        }
    }

    override fun releaseAllRunningPlayer() {
        runningMediaPlayerList.toSet().forEach { stopRunningPlayer(it) }
    }

    override fun isPlayerCurrentlyRunning() = runningMediaPlayerList.isNotEmpty()

    override fun listenToMediaPlayerStatus(): Observable<PlayerStatus> =
        mediaPlayingSubject.startWith(PlayerStatus.BOUND)

    private fun releaseRunningPlayerById(mediaId: Int) {
        runningMediaPlayerList.firstOrNull { it.mediaId == mediaId }?.let {
            releaseRunningPlayer(it)
        }
    }

    private fun releaseRunningPlayer(player: RunningPlayer) {
        runningMediaPlayerList.remove(player)
        player.simpleExoPlayer.release()
    }

    private fun stopRunningPlayer(player: RunningPlayer) {
        //stop() method will call the callback in SimpleExoPlayer listener with STATE_IDLE
        player.simpleExoPlayer.stop()
    }

}
