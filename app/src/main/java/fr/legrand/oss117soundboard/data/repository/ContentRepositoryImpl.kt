package fr.legrand.oss117soundboard.data.repository

import fr.legrand.oss117soundboard.data.entity.FilterType
import fr.legrand.oss117soundboard.data.entity.Movie
import fr.legrand.oss117soundboard.data.entity.MovieCharacter
import fr.legrand.oss117soundboard.data.entity.Reply
import fr.legrand.oss117soundboard.data.manager.file.FileManager
import fr.legrand.oss117soundboard.data.manager.media.MediaPlayerManager
import fr.legrand.oss117soundboard.data.manager.sharedpref.SharedPrefManager
import fr.legrand.oss117soundboard.data.manager.storage.StorageManager
import fr.legrand.oss117soundboard.data.values.SortType
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
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
    private val mediaPlayerManager: MediaPlayerManager
) : ContentRepository {

    private var startListenTimestamp = 0L

    override fun getReplyWithSearch(search: String, fromFavorite: Boolean): Observable<List<Reply>> {
        return Observable.defer {
            val replyList = storageManager.getReplyWithSearch(search, fromFavorite)
            val result = when (sharedPrefManager.getReplySort()) {
                SortType.ALPHABETICAL_SORT -> replyList.sortedBy { it.name }
                SortType.MOVIE_SORT -> replyList.sortedBy { it.timestamp }
                SortType.RANDOM_SORT -> replyList.shuffled()
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
                SortType.ALPHABETICAL_SORT -> replyList.sortedBy { it.name }
                SortType.MOVIE_SORT -> replyList.sortedBy { it.timestamp }
                SortType.RANDOM_SORT -> replyList.shuffled()
            }
            Observable.just(result)
        }
    }

    override fun updateMultiListenParameter(multiListen: Boolean): Completable {
        return Completable.defer {
            mediaPlayerManager.releaseAllRunningPlayer()
            sharedPrefManager.setMultiListenEnabled(multiListen)
            Completable.complete()
        }
    }

    override fun multiListenEnabled(): Observable<Boolean> {
        return Observable.fromCallable { sharedPrefManager.isMultiListenEnabled() }
    }

    override fun getMostListenedReply(): Observable<Reply> {
        return Observable.fromCallable { storageManager.getMostListenedReply() }
    }


    override fun playSoundMedia(replyId: Int): Completable {
        return Completable.defer {
            if (!mediaPlayerManager.isPlayerCurrentlyRunning()) {
                startListenTimestamp = System.currentTimeMillis()
            }

            mediaPlayerManager.playSoundMedia(replyId, sharedPrefManager.isMultiListenEnabled()).doOnComplete {
                storageManager.incrementReplyListenCount(replyId)
                if (!mediaPlayerManager.isPlayerCurrentlyRunning()) {
                    increaseTotalReplyTime(System.currentTimeMillis() - startListenTimestamp)
                }
            }
        }
    }

    override fun getAllFilters(): Single<List<FilterType>> =
        Single.defer { Single.just(FilterType.values().toList()) }

    override fun getAllMovies(): Single<List<Movie>> =
        Single.defer { Single.just(Movie.values().toList()) }

    override fun getAllCharacters(): Single<List<MovieCharacter>> =
        Single.defer { Single.just(MovieCharacter.values().toList()) }


    override fun getTotalReplyTime(): Observable<Long> {
        return Observable.fromCallable { sharedPrefManager.getTotalReplyTime() }
    }

    override fun listenToRandomReply(): Completable {
        return Completable.defer {
            val replyList = storageManager.getAllReply()
            val replyId = replyList[(0 until replyList.size).random()].id
            playSoundMedia(replyId)
        }
    }

    override fun updateReplySort(replySort: SortType): Completable {
        return Completable.defer {
            sharedPrefManager.setReplySort(replySort.name)
            Completable.complete()
        }
    }

    override fun releaseRunningPlayers(): Completable {
        return Completable.defer {
            mediaPlayerManager.releaseAllRunningPlayer()
            Completable.complete()
        }

    }

    override fun isPlayerRunning(): Single<Boolean> {
        return Single.defer {
            Single.just(mediaPlayerManager.isPlayerCurrentlyRunning())
        }
    }

    override fun getReplySort(): Single<SortType> {
        return Single.fromCallable { sharedPrefManager.getReplySort() }
    }


    private fun increaseTotalReplyTime(duration: Long) {
        sharedPrefManager.increaseTotalReplyTime(duration)
    }


}
