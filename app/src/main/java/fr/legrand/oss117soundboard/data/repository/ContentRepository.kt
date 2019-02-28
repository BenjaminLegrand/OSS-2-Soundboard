package fr.legrand.oss117soundboard.data.repository

import fr.legrand.oss117soundboard.data.entity.Reply
import io.reactivex.Completable
import io.reactivex.Observable

/**
 * Created by Benjamin on 30/09/2017.
 */

interface ContentRepository {
    fun getReplyWithSearch(search: String, fromFavorite: Boolean): Observable<List<Reply>>

    fun initAllReply(): Completable

    fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean): Completable

    fun getAllReply(fromFavorite: Boolean): Observable<List<Reply>>

    fun updateMultiListenParameter(multiListen: Boolean): Completable

    fun multiListenEnabled(): Observable<Boolean>

    fun getMostListenedReply(): Observable<Reply>

    fun incrementReplyListenCount(replyId: Int): Completable

    fun increaseTotalReplyTime(duration: Long)

    fun getTotalReplyTime(): Observable<Long>

    fun retrieveRandomReplyIdToListen(): Observable<Int>

    fun updateReplySort(replySort: String): Completable

    fun getReplySort(): Observable<String>
}
