package fr.legrand.oss117soundboard.presentation.ui.reply.ui

import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.utils.hide
import fr.legrand.oss117soundboard.presentation.utils.show

/**
 * Created by Benjamin on 30/09/2017.
 */

class ReplyListViewHolder(itemView: View, private val context: Context) : RecyclerView.ViewHolder(itemView) {

    private val replyName: TextView = itemView.findViewById(R.id.reply_search_result_view_holder_name)
    private val replyDescription: TextView = itemView.findViewById(R.id.reply_view_holder_description)
    private val listenButton: Button = itemView.findViewById(R.id.reply_view_holder_listen_button)
    private val favoriteButton: Button = itemView.findViewById(R.id.reply_view_holder_favorite_button)
    private val descriptionToggle: ImageView = itemView.findViewById(R.id.reply_view_holder_description_toggle)


    fun bindReply(
        replyViewData: ReplyViewData,
        onListenClickListener: (Int) -> Unit,
        onFavoriteClickListener: (Int, Boolean) -> Unit
    ) {
        replyName.text = replyViewData.getDisplayName()
        replyDescription.text = replyViewData.getFormattedDescription()
        if (replyViewData.isFavorite()) {
            favoriteButton.text = context.getString(R.string.delete)
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
                context.getDrawable(R.drawable.ic_favorite_remove),
                null,
                null,
                null
            )
        } else {
            favoriteButton.text = context.getString(R.string.favorite)
            favoriteButton.setCompoundDrawablesWithIntrinsicBounds(
                context.getDrawable(R.drawable.ic_favorite_add),
                null,
                null,
                null
            )
        }
        if (replyViewData.isExpanded) {
            replyDescription.show()
            descriptionToggle.setImageResource(R.drawable.ic_toggle_up)
        } else {
            replyDescription.hide()
            descriptionToggle.setImageResource(R.drawable.ic_toggle_down)
        }
        descriptionToggle.setOnClickListener {
            if (replyDescription.isVisible) {
                replyViewData.isExpanded = false
                replyDescription.hide()
                descriptionToggle.setImageResource(R.drawable.ic_toggle_down)
            } else {
                replyViewData.isExpanded = true
                replyDescription.show()
                descriptionToggle.setImageResource(R.drawable.ic_toggle_up)
            }
        }

        listenButton.setOnClickListener { onListenClickListener(replyViewData.getId()) }
        favoriteButton.setOnClickListener {
            onFavoriteClickListener(
                replyViewData.getId(),
                !replyViewData.isFavorite()
            )
        }
    }
}
