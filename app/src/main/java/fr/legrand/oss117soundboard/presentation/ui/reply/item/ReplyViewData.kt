package fr.legrand.oss117soundboard.presentation.ui.reply.item

import android.content.Context
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.Reply

/**
 * Created by Benjamin on 30/09/2017.
 */

data class ReplyViewData(private val reply: Reply) {

    var isExpanded: Boolean = false

    fun getFormattedDescription() = String.format("%s%s%s", "\"", reply.description, "\"")

    fun getMostListenedText(context: Context): String = if (reply.listenCount == 0) {
        context.getString(R.string.no_listened_reply)
    } else {
        context.getString(R.string.most_listened_reply_format, reply.name, reply.listenCount)
    }

    fun getId() = reply.id

    fun isFavorite() = reply.isFavorite

    fun getDisplayName() = reply.name

}