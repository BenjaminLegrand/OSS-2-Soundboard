package fr.legrand.oss117soundboard.data.repository

import android.content.ContentValues
import android.content.Context
import android.media.RingtoneManager
import android.net.Uri
import android.provider.MediaStore
import androidx.core.content.FileProvider
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
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject
import javax.inject.Singleton


/**
 * Created by Benjamin on 30/09/2017.
 */

private const val SHARED_REPLY_FILEPATH = "oss_soundboard/shared_reply.mp3"
private const val RINGTONE_REPLY_FILEPATH = "oss_soundboard/oss_ringtone.mp3"
private const val MP3_MIME_TYPE = "audio/mp3"

@Singleton
class ContentRepositoryImpl @Inject constructor(
    private val storageManager: StorageManager,
    private val sharedPrefManager: SharedPrefManager,
    private val fileManager: FileManager,
    private val mediaPlayerManager: MediaPlayerManager,
    private val context: Context
) : ContentRepository {


    private var startListenTimestamp = 0L

    override fun getReplyWithSearch(
        search: String,
        fromFavorite: Boolean
    ): Observable<List<Reply>> {
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
            storageManager.resetReplyList()
            storageManager.saveReplyList(fileManager.buildReplyListFromResource())
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

            mediaPlayerManager.playSoundMedia(replyId, sharedPrefManager.isMultiListenEnabled())
                .doOnComplete {
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
        return Single.defer {
            Single.just(sharedPrefManager.getReplySort())
        }
    }

    override fun isBackgroundListenEnabled(): Single<Boolean> {
        return Single.defer {
            Single.just(sharedPrefManager.isBackgroundListenEnabled())
        }
    }

    override fun updateBackgroundListenParameter(enabled: Boolean): Completable {
        return Completable.defer {
            sharedPrefManager.setBackgroundListenEnabled(enabled)
            Completable.complete()
        }
    }

    override fun generateShareData(replyId: Int): Single<Pair<Uri, String>> = Single.defer {
        val inputStream = context.resources.openRawResource(replyId)
        val file = File(
            context.filesDir
            , SHARED_REPLY_FILEPATH
        ).apply { parentFile.mkdirs() }
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(inputStream.readBytes())
        Single.just(
            Pair(
                FileProvider.getUriForFile(context, context.packageName, file),
                storageManager.getReplyById(replyId).name
            )
        )
    }

    override fun setReplyAsRingtone(replyId: Int): Completable = Completable.defer {
        val inputStream = context.resources.openRawResource(replyId)
        val file = File(
            context.externalCacheDir
            , RINGTONE_REPLY_FILEPATH
        ).apply { parentFile.mkdirs() }
        val fileOutputStream = FileOutputStream(file)
        fileOutputStream.write(inputStream.readBytes())

        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DATA, file.absolutePath)
            put(MediaStore.MediaColumns.TITLE, RINGTONE_REPLY_FILEPATH)
            put(MediaStore.MediaColumns.MIME_TYPE, MP3_MIME_TYPE)
            put(MediaStore.MediaColumns.SIZE, file.length())
            put(MediaStore.Audio.Media.IS_RINGTONE, true)
            put(MediaStore.Audio.Media.IS_NOTIFICATION, false)
            put(MediaStore.Audio.Media.IS_ALARM, false)
            put(MediaStore.Audio.Media.IS_MUSIC, false)
        }

        val mediaStoreContentUri = MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        context.contentResolver.delete(
            mediaStoreContentUri,
            MediaStore.MediaColumns.DATA + "='" + file.absolutePath + "'",
            null
        )
        RingtoneManager.setActualDefaultRingtoneUri(
            context,
            RingtoneManager.TYPE_RINGTONE,
            context.contentResolver.insert(mediaStoreContentUri, contentValues)
        )

        Completable.complete()
    }

    private fun increaseTotalReplyTime(duration: Long) {
        sharedPrefManager.increaseTotalReplyTime(duration)
    }
}
