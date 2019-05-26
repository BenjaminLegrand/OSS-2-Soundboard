package fr.legrand.oss117soundboard.presentation.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.component.dialog.DialogComponent
import fr.legrand.oss117soundboard.presentation.ui.base.BaseVMFragment
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_settings.*
import javax.inject.Inject

/**
 * Created by Benjamin on 17/10/2017.
 */

class SettingsFragment : BaseVMFragment<SettingsViewModel>() {
    override fun getLayoutId() = R.layout.fragment_settings

    override val viewModelClass = SettingsViewModel::class

    private lateinit var sharedViewModel: ReplySharedViewModel

    @Inject
    lateinit var dialogComponent: DialogComponent

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[ReplySharedViewModel::class.java]

        viewModel.mostListenedReply.observeSafe(this) {
            fragment_settings_most_listened_reply.text = it.getMostListenedText(context!!)
        }
        viewModel.replySort.observeSafe(this) { sort ->
            fragment_settings_reply_sort.text = sort.getSortText(context!!)
            fragment_settings_reply_sort_area.setOnClickListener { _ ->
                dialogComponent.displaySingleChoiceDialog(
                    R.string.reply_sort,
                    sort.sortString,
                    sort.getSelectedIndex(),
                    R.string.confirm,
                    {
                        viewModel.updateReplySort(sort.getValue(it))
                    },
                    R.string.cancel,
                    {})
            }
        }

        sharedViewModel.onReplyListenFinished.observeSafe(this) {
            viewModel.updateAllReplyData()
        }

        viewModel.totalReplyTime.observeSafe(this) {
            fragment_settings_total_reply_time.text =
                getString(R.string.total_reply_time_text, it.first, it.second, it.third)
        }

        viewModel.multiListenEnabled.observeSafe(this) {
            fragment_settings_select_multi_listen_switch.isChecked = it
            fragment_settings_select_multi_listen_switch.setOnCheckedChangeListener { _, checked ->
                viewModel.updateMultiListenParameter(checked)
            }
        }

        fragment_settings_random_reply_title.setOnClickListener { sharedViewModel.listenToRandomReply() }


    }


}
