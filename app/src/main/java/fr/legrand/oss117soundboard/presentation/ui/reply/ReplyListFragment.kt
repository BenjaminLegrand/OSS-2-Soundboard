package fr.legrand.oss117soundboard.presentation.ui.reply

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseVMFragment
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.ui.reply.ui.ReplyListAdapter
import fr.legrand.oss117soundboard.presentation.utils.hide
import fr.legrand.oss117soundboard.presentation.utils.observeSafe
import fr.legrand.oss117soundboard.presentation.utils.setToGrayScale
import fr.legrand.oss117soundboard.presentation.utils.show
import kotlinx.android.synthetic.main.fragment_reply_list.*
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class ReplyListFragment : BaseVMFragment<ReplyListViewModel>() {
    override val viewModelClass = ReplyListViewModel::class
    @Inject
    lateinit var replyListAdapter: ReplyListAdapter

    private val args by navArgs<ReplyListFragmentArgs>()
    private lateinit var sharedViewModel: ReplySharedViewModel

    override fun getLayoutId() = R.layout.fragment_reply_list

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[ReplySharedViewModel::class.java]

        initializeRecyclerView()

        fragment_reply_list_placeholder_image.setToGrayScale()

        sharedViewModel.onSearchRequested.observeSafe(this) {
            viewModel.updateSearch(it)
            viewModel.getAllReply(args.favorite)
        }

        viewModel.replyFavoriteUpdated.observeSafe(this) {
            viewModel.getAllReply(args.favorite)
        }

        viewModel.replyListLiveData.observeSafe(this) {
            replyListAdapter.setItems(it)
        }

        viewModel.viewState.observeSafe(this) {
            if (it.displayingPlaceholder) {
                displayPlaceholder()
            } else {
                displayReplyList()
            }
        }

        viewModel.getAllReply(args.favorite)
    }

    private fun displayPlaceholder() {
        fragment_reply_list_recycler.hide()
        fragment_reply_list_placeholder.show()
    }

    private fun displayReplyList() {
        fragment_reply_list_recycler.show()
        fragment_reply_list_placeholder.hide()
    }


    private fun initializeRecyclerView() {
        fragment_reply_list_recycler.layoutManager = LinearLayoutManager(activity)
        fragment_reply_list_recycler.adapter = replyListAdapter

        replyListAdapter.onListenClickListener = {
            sharedViewModel.listenToReply(it)
        }
        replyListAdapter.onFavoriteClickListener = { id, favorite ->
            viewModel.updateFavoriteReply(id, favorite)
        }
    }
}
