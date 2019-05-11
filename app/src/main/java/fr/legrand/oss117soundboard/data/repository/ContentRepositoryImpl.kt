package fr.legrand.oss117soundboard.data.repository

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.Reply
import fr.legrand.oss117soundboard.data.manager.file.FileManager
import fr.legrand.oss117soundboard.data.manager.sharedpref.SharedPrefManager
import fr.legrand.oss117soundboard.data.manager.storage.StorageManager
import fr.legrand.oss117soundboard.data.values.SortValues
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import java.util.concurrent.ThreadLocalRandom
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Benjamin on 30/09/2017.
 */
@Singleton
class ContentRepositoryImpl @Inject constructor(
        private val storageManager: StorageManager,
        private val sharedPrefManager: SharedPrefManager,
        private val fileManager: FileManager,
        private val context: Context
) : ContentRepository {

    override fun getReplyWithSearch(search: String, fromFavorite: Boolean): Observable<List<Reply>> {
        return Observable.defer {
            val replyList = storageManager.getReplyWithSearch(search, fromFavorite)
            val result = when (sharedPrefManager.getReplySort()) {
                SortValues.ALPHABETICAL_SORT -> replyList.sortedBy { it.name }
                SortValues.MOVIE_SORT -> replyList.sortedBy { it.timestamp }
                SortValues.RANDOM_SORT -> replyList.shuffled()
            }
            Observable.just(result)
        }
    }

    override fun initAllReply(): Completable {
        return Completable.defer {
            if (storageManager.getAllReply().isEmpty()) {
                val replyList = fileManager.buildReplyListFromResource()
                storageManager.saveReplyList(replyList)
            }
            Completable.complete()
        }
    }

    override fun updateFavoriteReply(replyId: Int, addToFavorite: Boolean): Completable {
        return Completable.defer {
            storageManager.updateFavoriteReply(replyId, addToFavorite)
            Completable.complete()
        }
    }

    override fun getAllReply(fromFavorite: Boolean): Observable<List<Reply>> {
        return Observable.defer {
            val replyList = storageManager.getAllReply(fromFavorite)
            val result = when (sharedPrefManager.getReplySort()) {
                SortValues.ALPHABETICAL_SORT -> replyList.sortedBy { it.name }
                SortValues.MOVIE_SORT -> replyList.sortedBy { it.timestamp }
                SortValues.RANDOM_SORT -> replyList.shuffled()
            }
            Observable.just(result)
        }
    }

    override fun updateMultiListenParameter(multiListen: Boolean): Completable {
        sharedPrefManager.setMultiListenEnabled(multiListen)
        return Completable.complete()
    }

    override fun multiListenEnabled(): Observable<Boolean> {
        return Observable.fromCallable { sharedPrefManager.isMultiListenEnabled() }
    }

    override fun getMostListenedReply(): Observable<Reply> {
        return Observable.fromCallable { storageManager.getMostListenedReply() }
    }

    override fun incrementReplyListenCount(replyId: Int): Completable {
        return Completable.defer {
            storageManager.incrementReplyListenCount(replyId)
            Completable.complete()
        }
    }

    override fun increaseTotalReplyTime(duration: Long) {
        sharedPrefManager.increaseTotalReplyTime(duration)
    }

    override fun getTotalReplyTime(): Observable<Long> {
        return Observable.fromCallable { sharedPrefManager.getTotalReplyTime() }
    }

    override fun retrieveRandomReplyIdToListen(): Observable<Int> {
        return Observable.defer {
            val replyList = storageManager.getAllReply()
            val randomNumber = ThreadLocalRandom.current().nextInt(0, replyList.size + 1)
            val replyId = replyList[randomNumber].id
            storageManager.incrementReplyListenCount(replyId)
            Observable.just(replyId)
        }
    }

    override fun updateReplySort(replySort: String): Completable {
        return Completable.defer {
            sharedPrefManager.setReplySort(replySort)
            Completable.complete()
        }
    }

    override fun getReplySort(): Single<SortValues> {
        return Single.defer { Single.just(sharedPrefManager.getReplySort()) }
    }
}
