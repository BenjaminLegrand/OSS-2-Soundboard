package fr.legrand.oss117soundboard.presentation.ui.reply

import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.navigator.MainNavigator
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

    @Inject
    lateinit var mainNavigator: MainNavigator

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

        sharedViewModel.onCharacterFilterUpdated.observeSafe(this) {
            viewModel.updateCharacterFilter(it)
            viewModel.getAllReply(args.favorite)
        }

        sharedViewModel.onMovieFilterUpdated.observeSafe(this) {
            viewModel.updateMovieFilter(it)
            viewModel.getAllReply(args.favorite)
        }

        viewModel.replyFavoriteUpdated.observeSafe(this) {
            viewModel.getAllReply(args.favorite)
        }

        viewModel.resultsIndicator.observeSafe(this) {
            val text = if (it == 0) {
                getString(R.string.results_indicator_zero)
            } else {
                resources.getQuantityString(R.plurals.results_indicator_other, it, it)
            }
            if (args.favorite) {
                search_results_end_indicator.text = text
            } else {
                search_results_start_indicator.text = text
            }
        }

        viewModel.replyListLiveData.observeSafe(this) {
            if (it.isEmpty()) {
                checkSearchResultsIndicator()
                displayPlaceholder()
            } else {
                search_results_start_indicator.hide()
                search_results_end_indicator.hide()
                displayReplyList()
                replyListAdapter.setItems(it)
            }
        }

        viewModel.replyShareData.observeSafe(this) {
            mainNavigator.shareReply(it.first, it.second)
        }

        search_results_start_indicator.setOnClickListener {
            findNavController().navigate(R.id.action_global_favorite_reply_fragment)
        }
        search_results_end_indicator.setOnClickListener {
            findNavController().navigate(R.id.action_global_all_reply_fragment)
        }
    }

    private fun checkSearchResultsIndicator() {
        if (sharedViewModel.onSearchRequested.value?.isNotEmpty() == true) {
            if (args.favorite) {
                search_results_end_indicator.show()
                search_results_start_indicator.hide()
            } else {
                search_results_start_indicator.show()
                search_results_end_indicator.hide()
            }
            viewModel.getAllReplyNegated(args.favorite)
        }
    }

    private fun displayPlaceholder() {
        fragment_reply_list_recycler.hide()
        fragment_reply_list_placeholder_image.show()
        fragment_reply_list_placeholder_text.show()
    }

    private fun displayReplyList() {
        fragment_reply_list_recycler.show()
        fragment_reply_list_placeholder_image.hide()
        fragment_reply_list_placeholder_text.hide()
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

        replyListAdapter.onShareClickListener = { id ->
            viewModel.generateReplyShareData(id)
        }

        replyListAdapter.onRingtoneClickListener = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (Settings.System.canWrite(context)) {
                    viewModel.setReplyAsRingtone(it)
                }else{
                    mainNavigator.requestStartSettingsWritePermission()
                }
            } else {
                viewModel.setReplyAsRingtone(it)
            }
        }
    }
}
