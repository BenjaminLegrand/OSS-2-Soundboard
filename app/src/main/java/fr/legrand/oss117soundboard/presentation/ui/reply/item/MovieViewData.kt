package fr.legrand.oss117soundboard.presentation.ui.reply.item

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.Movie

class MovieViewData(private val movie: Movie) {
    fun getDisplayName(context: Context): String = when (movie) {
        Movie.OSS_1 -> context.getString(R.string.oss_1_movie_name)
        Movie.OSS_2 -> context.getString(R.string.oss_2_movie_name)
        Movie.UNKNOWN -> context.getString(R.string.unknown_movie)
    }
}