package fr.legrand.oss117soundboard.data.manager.sharedpref

/**
 * Created by Benjamin on 30/09/2017.
 */

interface SharedPrefManager {
    fun isMultiListenEnabled(): Boolean

    fun setMultiListenEnabled(multiListen: Boolean)

    fun increaseTotalReplyTime(duration: Long)

    fun getTotalReplyTime(): Long

    fun getReplySort(): String

    fun setReplySort(replySort: String)
}
