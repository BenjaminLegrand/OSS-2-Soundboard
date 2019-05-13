package fr.legrand.oss117soundboard.data.manager.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.legrand.oss117soundboard.data.entity.Reply

@Database(version = 1, entities = [Reply::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomManager(): RoomManager
}