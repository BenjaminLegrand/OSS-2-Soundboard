package fr.legrand.oss117soundboard.data.entity

/**
 * Created by Benjamin on 30/09/2017.
 */
data class Reply(
    var id: Int = 0,
    var name: String = "",
    var description: String = "",
    var isFavorite: Boolean = false,
    var listenCount: Int = 0,
    var timestamp: Int = 0
) {

    constructor(id: Int, name: String, description: String, timestamp: Int) : this() {
        this.id = id
        this.name = name
        this.description = description
        this.timestamp = timestamp
        this.isFavorite = false
    }

}
