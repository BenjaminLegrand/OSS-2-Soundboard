package fr.legrand.oss117soundboard.data.manager.file

import fr.legrand.oss117soundboard.data.entity.Reply

/**
 * Created by Benjamin on 30/09/2017.
 */

interface FileManager {
    fun buildReplyListFromResource(): List<Reply>
}
