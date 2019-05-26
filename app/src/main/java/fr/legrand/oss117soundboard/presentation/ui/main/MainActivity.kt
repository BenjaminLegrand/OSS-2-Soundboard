package fr.legrand.oss117soundboard.presentation.ui.main

import android.media.AudioManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.ui.setupWithNavController
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.entity.FilterType
import fr.legrand.oss117soundboard.presentation.component.dialog.DialogComponent
import fr.legrand.oss117soundboard.presentation.navigator.MainNavigator
import fr.legrand.oss117soundboard.presentation.ui.base.BaseVMActivity
import fr.legrand.oss117soundboard.presentation.ui.base.hideKeyboard
import fr.legrand.oss117soundboard.presentation.utils.StringUtils
import fr.legrand.oss117soundboard.presentation.utils.hide
import fr.legrand.oss117soundboard.presentation.utils.observeSafe
import fr.legrand.oss117soundboard.presentation.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */

class MainActivity : BaseVMActivity<MainViewModel>() {

    override val viewModelClass = MainViewModel::class

    private lateinit var replySharedViewModel: ReplySharedViewModel

    @Inject
    lateinit var mainNavigator: MainNavigator
    @Inject
    lateinit var dialogComponent: DialogComponent

    override fun getLayoutId() = R.layout.activity_main
    override fun getNavHostId() = fragment_container.id

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //We change the media volume affected by hardware buttons
        volumeControlStream = AudioManager.STREAM_MUSIC

        setSupportActionBar(activity_main_toolbar)

        main_activity_bottom_view.setupWithNavController(navController)

        replySharedViewModel = ViewModelProviders.of(this, viewModelFactory)[ReplySharedViewModel::class.java]

        replySharedViewModel.onReplyListenFinished.observeSafe(this) {
            main_activity_fab_stop_listen.hide()
        }
        replySharedViewModel.onListenRequested.observeSafe(this) {
            main_activity_fab_stop_listen.show()
        }

        main_activity_fab_stop_listen.setOnClickListener {
            viewModel.releaseRunningPlayers()
            main_activity_fab_stop_listen.hide()
        }

        initializeSearch()
    }

    override fun onPause() {
        super.onPause()
        viewModel.releaseRunningPlayers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        replySharedViewModel.availableFilters.observeSafe(this) {
            val subMenu = menu.findItem(R.id.menu_filter).subMenu
            subMenu.clear()
            it.forEach { filter ->
                subMenu.add(
                    R.id.menu_filter_type_group,
                    filter.hashCode(), 0, filter.getDisplayName(this)
                ).apply {
                    isCheckable = true
                    setOnMenuItemClickListener {
                        displayFilterDialog(filter.getType()) { selected -> isChecked = selected.isNotEmpty() }
                        true
                    }
                }
            }
            subMenu.setGroupCheckable(R.id.menu_filter_type_group, true, false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_search -> {
                if (activity_main_layout_search.isVisible) {
                    activity_main_layout_search.hide()
                    activity_main_search.setText("")
                    item.icon = getDrawable(R.drawable.ic_search)
                    hideKeyboard(main_activity_root_layout)
                    replySharedViewModel.requestSearch(ReplySharedViewModel.NO_SEARCH)
                } else {
                    item.icon = getDrawable(R.drawable.ic_close)
                    activity_main_layout_search.show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initializeSearch() {
        activity_main_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.matches(StringUtils.SEARCH_EASTER_EGG_REGEX.toRegex())) {
                    mainNavigator.launchBrowsingApp()
                } else {
                    replySharedViewModel.requestSearch(s.toString())
                }
            }

        })
    }

    private fun displayFilterDialog(filter: FilterType, onFilterSelected: (IntArray) -> Unit) {
        when (filter) {
            FilterType.CHARACTERS -> dialogComponent.displayMultiChoiceDialog(
                title = R.string.filter_characters,
                choices = replySharedViewModel.characterFilters.map { it.getDisplayName(this) },
                selected = replySharedViewModel.characterFilters.mapIndexedNotNull { index, character ->
                    if (character.selected) {
                        index
                    } else {
                        null
                    }
                }.toIntArray(),
                positiveText = R.string.confirm,
                onPositiveClick = {
                    onFilterSelected(it)
                    replySharedViewModel.selectCharacterFilters(it)
                },
                negativeText = R.string.cancel,
                onNegativeClick = {}
            )
        }
    }
}
