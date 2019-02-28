package fr.legrand.oss117soundboard.data.manager.storage

import fr.legrand.oss117soundboard.data.entity.Reply
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StorageManagerImpl @Inject constructor() : StorageManager {

    private val replyList = mutableListOf<Reply>()

    override fun getMostListenedReply(): Reply = replyList.maxBy { it.listenCount } ?: replyList.first()

    override fun getReplyWithSearch(search: String, fromFavorite: Boolean) = replyList.filter {
        it.isFavorite == fromFavorite &&
                (it.description.toLowerCase().contains(search.toLowerCase()) || it.name.toLowerCase().contains(search.toLowerCase()))
    }

    override fun getAllReply(fromFavorite: Boolean) = replyList.filter { it.isFavorite == fromFavorite }

    override fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean) {
        replyList.find { it.id == replyId }?.isFavorite = addToFavorite
    }

    override fun saveReplyList(replyList: List<Reply>) {
        this.replyList.clear()
        this.replyList.addAll(replyList)
    }

    override fun incrementReplyListenCount(replyId: Int) {
        replyList.find { it.id == replyId }?.listenCount?.plus(1)
    }

    override fun getAllReply(): List<Reply> = replyList
}