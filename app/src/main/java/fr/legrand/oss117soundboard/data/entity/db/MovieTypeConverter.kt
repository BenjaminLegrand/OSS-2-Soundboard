package fr.legrand.oss117soundboard.data.entity.db

import androidx.room.TypeConverter
import fr.legrand.oss117soundboard.data.entity.Movie


class MovieTypeConverter {
    @TypeConverter
    fun toMovie(value: String): Movie = Movie.valueOf(value)

    @TypeConverter
    fun toValue(movie: Movie): String {
        return movie.name
    }
}