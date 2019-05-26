package fr.legrand.oss117soundboard.data.entity

import androidx.room.*
import fr.legrand.oss117soundboard.data.entity.db.MovieTypeConverter

/**
 * Created by Benjamin on 30/09/2017.
 */
@Entity
data class Reply(
        @PrimaryKey
        var id: Int = 0,
        @ColumnInfo
        var name: String = "",
        @ColumnInfo
        var description: String = "",
        @ColumnInfo
        var isFavorite: Boolean = false,
        @ColumnInfo
        var movie: Movie = Movie.UNKNOWN,
        @ColumnInfo
        var listenCount: Int = 0,
        @ColumnInfo
        var timestamp: Int = 0
) {

    constructor(id: Int, name: String, description: String, timestamp: Int, movie: Movie) : this() {
        this.id = id
        this.name = name
        this.description = description
        this.timestamp = timestamp
        this.movie = movie
        this.isFavorite = false
    }

}
