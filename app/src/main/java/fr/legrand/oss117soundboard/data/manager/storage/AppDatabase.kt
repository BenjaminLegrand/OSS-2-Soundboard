package fr.legrand.oss117soundboard.data.manager.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.legrand.oss117soundboard.data.entity.Reply
import fr.legrand.oss117soundboard.data.entity.db.MovieTypeConverter

@Database(version = 1, entities = [Reply::class], exportSchema = false)
@TypeConverters(MovieTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomManager(): RoomManager
}