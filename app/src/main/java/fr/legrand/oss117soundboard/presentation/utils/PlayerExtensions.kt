package fr.legrand.oss117soundboard.presentation.utils

import android.content.Context
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.upstream.DataSpec
import com.google.android.exoplayer2.upstream.RawResourceDataSource

fun SimpleExoPlayer.onStopListener(onStop: () -> (Unit), onError: (Throwable) -> (Unit)) {
    addListener(object : Player.EventListener {
        override fun onSeekProcessed() {}

        override fun onPositionDiscontinuity(reason: Int) {}

        override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}

        override fun onTimelineChanged(timeline: Timeline?, manifest: Any?, reason: Int) {}

        override fun onTracksChanged(trackGroupArray: TrackGroupArray, trackSelectionArray: TrackSelectionArray) {}

        override fun onLoadingChanged(b: Boolean) {}

        override fun onPlayerStateChanged(b: Boolean, state: Int) {
            //Media ended or we used the stop() method
            if (state == Player.STATE_ENDED || state == Player.STATE_IDLE) {
                onStop()
            }
        }

        override fun onRepeatModeChanged(i: Int) {}

        override fun onPlayerError(e: ExoPlaybackException) {
            onError(e)
        }

        override fun onPlaybackParametersChanged(playbackParameters: PlaybackParameters) {}
    })
}

fun SimpleExoPlayer.startMedia(context: Context, mediaId: Int, onError: (Throwable) -> Unit) {
    val soundUri = RawResourceDataSource.buildRawResourceUri(mediaId)
    val dataSource = RawResourceDataSource(context)

    try {
        dataSource.open(DataSpec(soundUri))
        val soundSource = ExtractorMediaSource.Factory { dataSource }
        prepare(soundSource.createMediaSource(soundUri))
        playWhenReady = true
    } catch (e: Exception) {
        onError(e)
    }
}