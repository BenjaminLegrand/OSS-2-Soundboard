package fr.legrand.oss117soundboard.data.repository

import fr.legrand.oss117soundboard.data.entity.FilterType
import fr.legrand.oss117soundboard.data.entity.MovieCharacter
import fr.legrand.oss117soundboard.data.entity.Reply
import fr.legrand.oss117soundboard.data.values.SortType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

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

    fun getTotalReplyTime(): Observable<Long>

    fun updateReplySort(replySort: SortType): Completable

    fun getReplySort(): Single<SortType>

    fun playSoundMedia(replyId: Int): Completable

    fun listenToRandomReply(): Completable

    fun releaseRunningPlayers() : Completable

    fun isPlayerRunning() : Single<Boolean>

    fun getAllFilters(): Single<List<FilterType>>

    fun getAllCharacters(): Single<List<MovieCharacter>>
}
