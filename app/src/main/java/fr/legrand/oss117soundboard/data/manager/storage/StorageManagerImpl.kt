package fr.legrand.oss117soundboard.data.manager.storage

import android.content.Context
import androidx.room.Room
import fr.legrand.oss117soundboard.data.entity.Reply
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManagerImpl @Inject constructor(context: Context) : StorageManager {

    private val roomManager: RoomManager =
            Room.databaseBuilder(context, AppDatabase::class.java, "database").build().roomManager()

    override fun getMostListenedReply(): Reply = roomManager.getMostListenedReply()

    override fun getReplyWithSearch(search: String, fromFavorite: Boolean) =
            roomManager.getReplyWithSearch(search.toLowerCase(), fromFavorite)

    override fun getAllReply(fromFavorite: Boolean) = roomManager.getAllReply(fromFavorite)

    override fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean) {
        roomManager.updateFavoriteReply(replyId, addToFavorite)
    }

    override fun saveReplyList(replyList: List<Reply>) {
        roomManager.saveReplyList(replyList)
    }

    override fun incrementReplyListenCount(replyId: Int) {
        roomManager.incrementReplyListenCount(replyId)
    }

    override fun getAllReply(): List<Reply> = roomManager.getAllReply()

    override fun getReplyById(replyId: Int): Reply = roomManager.getReplyById(replyId)
    override fun resetReplyList() = roomManager.resetReplyList()

}