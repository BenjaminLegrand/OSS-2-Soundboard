package fr.legrand.oss117soundboard.presentation.ui.settings

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseVMFragment
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.utils.observeSafe
import kotlinx.android.synthetic.main.fragment_settings.*

/**
 * Created by Benjamin on 17/10/2017.
 */

class SettingsFragment : BaseVMFragment<SettingsViewModel>() {
    override fun getLayoutId() = R.layout.fragment_settings

    override val viewModelClass = SettingsViewModel::class

    private lateinit var sharedViewModel: ReplySharedViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProviders.of(activity!!, viewModelFactory)[ReplySharedViewModel::class.java]

        sharedViewModel.onReplyListened.observeSafe(this) {
            viewModel.updateAllReplyData()
        }
        viewModel.mostListenedReply.observeSafe(this) {
            fragment_settings_most_listened_reply.text = it.getMostListenedText()
        }
        viewModel.replySort.observeSafe(this) {
            sharedViewModel.onSortUpdated()
            fragment_settings_reply_sort.text = it
        }

        viewModel.totalReplyTime.observeSafe(this) {
            fragment_settings_total_reply_time.text = getString(R.string.total_reply_time_text, it.first, it.second, it.third)
        }

        viewModel.viewState.observeSafe(this) {
            if (!it.mostListenedReplyAvailable) {
                fragment_settings_most_listened_reply.text = getString(R.string.no_listened_reply)
            }
        }

        viewModel.multiListenEnabled.observeSafe(this) {
            fragment_settings_select_multi_listen_switch.isChecked = it
        }

        fragment_settings_select_multi_listen_switch.setOnCheckedChangeListener { _, checked ->
            viewModel.updateMultiListenParameter(checked)
        }

        fragment_settings_random_reply_title.setOnClickListener { viewModel.listenToRandomReply() }
    }

//    fun updateSwitch(checked: Boolean) {
//        multiListenPreference!!.isChecked = checked
//    }
//
//    fun updateMostListenedReply(replyViewData: ReplyViewData?) {
//        if (replyViewData != null) {
//            mostListenedReplyPreference!!.summary = replyViewData.getMostListenedText()
//        } else {
//
//        }
//    }
//
//    fun updateReplySort(replySort: String) {
//        replySortPreference!!.summary = replySort
//    }
//
//    fun updateTotalReplyTime(hours: Long, minutes: Long, seconds: Long) {
//        totalReplyTimePreference!!.summary = getString(R.string.total_reply_time_text, hours, minutes, seconds)
//    }
//
//    private fun initializePreferences() {
//        multiListenPreference = this.getPreferenceManager().findPreference(getString(R.string.multi_listen_preference_key))
//        replySortPreference = this.getPreferenceManager().findPreference(getString(R.string.reply_sort_preference_key))
//        mostListenedReplyPreference = this.getPreferenceManager().findPreference(getString(R.string.most_listened_reply_preference_key))
//        totalReplyTimePreference = this.getPreferenceManager().findPreference(getString(R.string.total_reply_time_preference_key))
//        randomReplyPreference = this.getPreferenceManager().findPreference(getString(R.string.random_reply_preference_key))
//
//        multiListenPreference!!.setOnPreferenceChangeListener { preference, newValue ->
//            viewModel.updateMultiListenParameter(newValue as Boolean)
//            true
//        }
//
//        randomReplyPreference!!.setOnPreferenceClickListener { preference ->
//            viewModel.listenToRandomReply()
//            true
//        }
//        replySortPreference!!.setOnPreferenceChangeListener { preference, newValue ->
//            viewModel.updateReplySort(newValue as String)
//            replySortPreference!!.summary = newValue
//            true
//        }
//    }

}
