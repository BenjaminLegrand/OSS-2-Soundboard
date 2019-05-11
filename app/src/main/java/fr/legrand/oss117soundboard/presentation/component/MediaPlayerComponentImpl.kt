package fr.legrand.oss117soundboard.presentation.component

import android.content.Context
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import fr.legrand.oss117soundboard.data.entity.RunningPlayer
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.presentation.utils.onStopListener
import fr.legrand.oss117soundboard.presentation.utils.startMedia
import io.reactivex.Completable
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Benjamin on 30/09/2017.
 */

private const val MULTI_LISTEN_NUMBER_LIMIT = 8

@Singleton
class MediaPlayerComponentImpl @Inject constructor(
        private val contentRepository: ContentRepository,
        private val context: Context
) : MediaPlayerComponent {

    private val runningMediaPlayerList = mutableListOf<RunningPlayer>()

    private var startListenTimestamp = 0L

    override fun playSoundMedia(mediaId: Int): Completable {
        return Completable.create { emitter ->
            val videoTrackSelectionFactory = AdaptiveTrackSelection.Factory(null)
            val trackSelector = DefaultTrackSelector(videoTrackSelectionFactory)
            val simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

            simpleExoPlayer.startMedia(context, mediaId) {
                emitter.onError(it)
            }

            simpleExoPlayer.onStopListener(onStop = {
                releaseRunningPlayerById(mediaId)
                emitter.onComplete()
            }, onError = {
                emitter.onError(it)
            })

            if (startListenTimestamp == 0L) {
                startListenTimestamp = System.currentTimeMillis()
            }

            if (runningMediaPlayerList.isNotEmpty()
                    && runningMediaPlayerList.size >= MULTI_LISTEN_NUMBER_LIMIT) {
                stopRunningPlayer(runningMediaPlayerList[0])
            }
            runningMediaPlayerList.add(RunningPlayer(simpleExoPlayer, mediaId))
        }
    }

    override fun releaseAllRunningPlayer() {
        runningMediaPlayerList.forEach { stopRunningPlayer(it) }
        runningMediaPlayerList.clear()
    }

    override fun isPlayerCurrentlyRunning() = runningMediaPlayerList.isNotEmpty()

    private fun releaseRunningPlayerById(mediaId: Int) {
        releaseRunningPlayer(runningMediaPlayerList.first { it.mediaId == mediaId })
    }

    private fun releaseRunningPlayer(player: RunningPlayer) {
        runningMediaPlayerList.remove(player)
        player.simpleExoPlayer.release()

        if (runningMediaPlayerList.isEmpty()) {
            contentRepository.increaseTotalReplyTime(System.currentTimeMillis() - startListenTimestamp)
            startListenTimestamp = 0
        }
    }

    private fun stopRunningPlayer(player: RunningPlayer) {
        //stop() method will call the callback in SimpleExoPlayer listener with STATE_IDLE
        player.simpleExoPlayer.stop()
    }

}
