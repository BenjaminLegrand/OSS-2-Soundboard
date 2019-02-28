package fr.legrand.oss117soundboard.presentation.ui.reply.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import fr.legrand.oss117soundboard.presentation.ui.reply.item.ReplyViewData
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */
class ReplyListAdapter @Inject
constructor(private val activity: BaseActivity) : RecyclerView.Adapter<ReplyListViewHolder>() {

    private val replyList = mutableListOf<ReplyViewData>()

    var onListenClickListener: (Int) -> (Unit) = {}
    var onFavoriteClickListener: (Int, Boolean) -> (Unit) = { _, _ -> }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReplyListViewHolder {
        val resultView = LayoutInflater.from(activity).inflate(R.layout.reply_view_holder, parent, false)
        return ReplyListViewHolder(resultView, activity)
    }

    override fun onBindViewHolder(holder: ReplyListViewHolder, position: Int) {
        holder.bindReply(replyList[position], onListenClickListener, onFavoriteClickListener)
    }

    override fun getItemCount(): Int {
        return replyList.size
    }

    fun setItems(replyList: List<ReplyViewData>) {
        this.replyList.clear()
        this.replyList.addAll(replyList)
        notifyDataSetChanged()
    }

}
