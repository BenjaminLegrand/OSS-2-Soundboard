package fr.legrand.oss117soundboard.data.manager.storage

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.legrand.oss117soundboard.data.entity.Reply

@Dao
interface RoomManager {
    @Query("SELECT * FROM reply")
    fun getAllReply(): List<Reply>

    @Query("SELECT * FROM reply ORDER BY listenCount DESC LIMIT 1")
    fun getMostListenedReply(): Reply

    @Query("SELECT * FROM reply WHERE isFavorite == :fromFavorite AND (description LIKE '%' || :search || '%' OR name LIKE '%' || :search || '%')")
    fun getReplyWithSearch(search: String, fromFavorite: Boolean): List<Reply>

    @Query("SELECT * FROM reply WHERE isFavorite == :fromFavorite")
    fun getAllReply(fromFavorite: Boolean): List<Reply>

    @Query("UPDATE Reply SET isFavorite = :addToFavorite WHERE id = :replyId")
    fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean)

    @Insert
    fun saveReplyList(replyList: List<Reply>)

    @Query("UPDATE Reply SET listenCount = listenCount + 1 WHERE id = :replyId")
    fun incrementReplyListenCount(replyId: Int)

    @Query("SELECT * FROM reply WHERE id == :id")
    fun getReplyById(id: Int): Reply

    @Query("DELETE FROM reply")
    fun resetReplyList()

}