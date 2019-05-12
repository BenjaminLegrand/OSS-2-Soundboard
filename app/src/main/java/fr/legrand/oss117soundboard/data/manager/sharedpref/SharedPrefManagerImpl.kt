package fr.legrand.oss117soundboard.data.manager.sharedpref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import androidx.core.content.edit
import fr.legrand.oss117soundboard.data.values.SortType
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Benjamin on 30/09/2017.
 */

private const val MULTI_LISTEN_KEY = "MULTI_LISTEN_KEY"
private const val TOTAL_REPLY_TIME_KEY = "TOTAL_REPLY_TIME_KEY"
private const val REPLY_SORT_KEY = "REPLY_SORT_KEY"

@Singleton
class SharedPrefManagerImpl @Inject
constructor(context: Context) : SharedPrefManager {

    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    override fun isMultiListenEnabled(): Boolean {
        return sharedPreferences.getBoolean(MULTI_LISTEN_KEY, false)
    }

    override fun setMultiListenEnabled(multiListen: Boolean) {
        sharedPreferences.edit {
            putBoolean(MULTI_LISTEN_KEY, multiListen)
        }
    }

    override fun increaseTotalReplyTime(duration: Long) {
        sharedPreferences.edit {
            putLong(TOTAL_REPLY_TIME_KEY, sharedPreferences.getLong(TOTAL_REPLY_TIME_KEY, 0) + duration)
        }
    }

    override fun getTotalReplyTime(): Long {
        return sharedPreferences.getLong(TOTAL_REPLY_TIME_KEY, 0)
    }

    override fun getReplySort(): SortType {
        val sort = sharedPreferences.getString(REPLY_SORT_KEY, SortType.ALPHABETICAL_SORT.name) ?: SortType.ALPHABETICAL_SORT.name
        return SortType.valueOf(sort)
    }

    override fun setReplySort(replySort: String) {
        sharedPreferences.edit {
            putString(REPLY_SORT_KEY, replySort)
        }
    }
}
