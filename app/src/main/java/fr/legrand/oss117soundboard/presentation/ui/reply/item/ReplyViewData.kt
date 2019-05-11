package fr.legrand.oss117soundboard.presentation.ui.reply.item

import fr.legrand.oss117soundboard.data.entity.Reply

/**
 * Created by Benjamin on 30/09/2017.
 */

data class ReplyViewData(private val reply: Reply) {

    var isExpanded: Boolean = false

    fun getFormattedDescription() = String.format("%s%s%s", "\"", reply.description, "\"")

    fun getMostListenedText() = String.format("%s (%d)", reply.name, reply.listenCount)

    fun getId() = reply.id

    fun isFavorite() = reply.isFavorite

    fun getDisplayName() = reply.name

}
