package fr.legrand.oss117soundboard.data.entity

import com.google.android.exoplayer2.SimpleExoPlayer

/**
 * Created by Benjamin on 17/10/2017.
 */

data class RunningPlayer(
        var simpleExoPlayer: SimpleExoPlayer,
        var mediaId: Int
)
