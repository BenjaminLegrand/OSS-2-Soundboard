package fr.legrand.oss117soundboard.presentation.ui.reply.ui

import android.animation.AnimatorInflater
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import fr.legrand.oss117soundboard.presentation.utils.hide
import fr.legrand.oss117soundboard.presentation.utils.show

/**
 * Created by Benjamin on 30/09/2017.
 */

private const val EXPANDED_ROTATION = 180f
private const val COLLAPSED_ROTATION = 0f

class ReplyListViewHolder(itemView: View, private val context: Context) :
    RecyclerView.ViewHolder(itemView) {

    private val replyName: TextView = itemView.findViewById(R.id.reply_view_holder_name)
    private val replyDescription: TextView =
        itemView.findViewById(R.id.reply_view_holder_description)
    private val replyDescriptionIcon: ImageView =
        itemView.findViewById(R.id.reply_view_holder_description_icon)
    private val replyCharacters: TextView = itemView.findViewById(R.id.reply_view_holder_characters)
    private val listenButton: Button = itemView.findViewById(R.id.reply_view_holder_listen_button)
    private val favoriteButton: Button =
        itemView.findViewById(R.id.reply_view_holder_favorite_button)
    private val descriptionToggle: ImageView =
        itemView.findViewById(R.id.reply_view_holder_description_toggle)
    private val collapseArea: View = itemView.findViewById(R.id.reply_view_holder_collapse_area)
    private val overflow: ImageView = itemView.findViewById(R.id.reply_view_holder_overflow)


    fun bindReply(
        replyViewData: ReplyViewData,
        onListenClickListener: (Int) -> Unit,
        onFavoriteClickListener: (Int, Boolean) -> Unit,
        onReplySharedClickListener: (Int) -> Unit,
        onReplyRingtoneClickListener: (Int) -> Unit
    ) {
        replyName.text = replyViewData.getDisplayName()
        replyDescription.text = replyViewData.getFormattedDescription()
        replyCharacters.text = replyViewData.getCharactersText(context)
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

        val rotateExpandAnimator =
            AnimatorInflater.loadAnimator(context, R.animator.rotate_expand) as ObjectAnimator
        rotateExpandAnimator.target = descriptionToggle
        val rotateCollapseAnimator =
            AnimatorInflater.loadAnimator(context, R.animator.rotate_collapse) as ObjectAnimator
        rotateCollapseAnimator.target = descriptionToggle

        if (replyViewData.isExpanded) {
            replyDescription.show()
            replyDescriptionIcon.show()
            descriptionToggle.rotation = EXPANDED_ROTATION
        } else {
            replyDescription.hide()
            replyDescriptionIcon.hide()
            descriptionToggle.rotation = COLLAPSED_ROTATION
        }
        collapseArea.setOnClickListener {
            if (replyDescription.isVisible) {
                replyViewData.isExpanded = false
                replyDescription.hide()
                replyDescriptionIcon.hide()
                rotateExpandAnimator.cancel()
                rotateCollapseAnimator.start()
            } else {
                replyViewData.isExpanded = true
                replyDescription.show()
                replyDescriptionIcon.show()
                rotateCollapseAnimator.cancel()
                rotateExpandAnimator.start()
            }
        }

        listenButton.setOnClickListener { onListenClickListener(replyViewData.getId()) }
        favoriteButton.setOnClickListener {
            onFavoriteClickListener(
                replyViewData.getId(),
                !replyViewData.isFavorite()
            )
        }

        var popupMenu = PopupMenu(context, overflow)
        overflow.setOnClickListener {
            popupMenu.dismiss()
            popupMenu = PopupMenu(context, overflow)
            popupMenu.run {
                setOnMenuItemClickListener {
                    when (it.itemId) {
                        R.id.reply_overflow_share -> onReplySharedClickListener(replyViewData.getId())
                        R.id.reply_overflow_ringtone -> onReplyRingtoneClickListener(replyViewData.getId())
                    }
                    false
                }
                inflate(R.menu.reply_overflow_menu)
                show()
            }
        }
    }
}
