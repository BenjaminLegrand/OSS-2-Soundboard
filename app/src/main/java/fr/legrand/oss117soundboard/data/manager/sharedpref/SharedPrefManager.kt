package fr.legrand.oss117soundboard.data.manager.sharedpref

import fr.legrand.oss117soundboard.data.values.SortValues

/**
 * Created by Benjamin on 30/09/2017.
 */

interface SharedPrefManager {
    fun isMultiListenEnabled(): Boolean

    fun setMultiListenEnabled(multiListen: Boolean)

    fun increaseTotalReplyTime(duration: Long)

    fun getTotalReplyTime(): Long

    fun getReplySort(): SortValues

    fun setReplySort(replySort: String)
}
