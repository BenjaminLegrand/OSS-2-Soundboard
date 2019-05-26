package fr.legrand.oss117soundboard.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

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
    var characters: MutableList<MovieCharacter> = mutableListOf(),
    @ColumnInfo
    var listenCount: Int = 0,
    @ColumnInfo
    var timestamp: Int = 0
) {


    constructor(
        id: Int,
        name: String,
        description: String,
        timestamp: Int,
        movie: Movie,
        characters: List<MovieCharacter>
    ) : this() {
        this.id = id
        this.name = name
        this.description = description
        this.timestamp = timestamp
        this.movie = movie
        this.characters = characters.toMutableList()
        this.isFavorite = false
    }

}
