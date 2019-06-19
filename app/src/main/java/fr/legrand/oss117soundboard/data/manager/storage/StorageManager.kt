package fr.legrand.oss117soundboard.data.manager.storage

import fr.legrand.oss117soundboard.data.entity.Reply

interface StorageManager {
    fun getAllReply(): List<Reply>

    fun getMostListenedReply(): Reply

    fun getReplyWithSearch(search: String, fromFavorite: Boolean): List<Reply>

    fun getAllReply(fromFavorite: Boolean): List<Reply>

    fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean)

    fun saveReplyList(replyList: List<Reply>)

    fun incrementReplyListenCount(replyId: Int)

    fun getReplyById(replyId: Int): Reply

    fun resetReplyList()

}