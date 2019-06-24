package fr.legrand.oss117soundboard.presentation.ui.main

import android.media.AudioManager
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.Menu
import android.view.inputmethod.EditorInfo
import android.widget.TextView
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
import kotlin.math.min

/**
 * Created by Benjamin on 30/09/2017.
 */

private const val SINGLE_FILTER_DISPLAY_COUNT_LIMIT = 2

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

        replySharedViewModel =
            ViewModelProviders.of(this, viewModelFactory)[ReplySharedViewModel::class.java]

        replySharedViewModel.onReplyListenFinished.observeSafe(this) {
            main_activity_fab_stop_listen.hide()
        }
        replySharedViewModel.onListenRequested.observeSafe(this) {
            main_activity_fab_stop_listen.show()
        }

        main_activity_fab_stop_listen.setOnClickListener {
            replySharedViewModel.releaseRunningPlayers()
            main_activity_fab_stop_listen.hide()
        }

        replySharedViewModel.activeFiltersUpdated.observeSafe(this) {
            if (it.isNotEmpty()) {
                updateFilterIndicator(it)
            } else {
                activity_main_filter_indicator_layout.removeAllViews()
                activity_main_filter_group.hide()
            }
        }

        navController.addOnDestinationChangedListener { _, dest, _ ->
            if (dest.id == R.id.settings_fragment) {
                activity_main_search_group.hide()
                activity_main_filter_group.hide()
            } else {
                activity_main_search_group.show()
                if (replySharedViewModel.activeFilters.isNotEmpty()) {
                    activity_main_filter_group.show()
                }
            }
            clearSearchFocus()
            invalidateOptionsMenu()
        }

        initializeSearch()
    }


    override fun onStop() {
        super.onStop()
        replySharedViewModel.releaseRunningPlayersBackground()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        if (navController.currentDestination?.id == R.id.settings_fragment) {
            return super.onCreateOptionsMenu(menu)
        }
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

                    isChecked = when (filter.getType()) {
                        FilterType.CHARACTERS -> replySharedViewModel.onCharacterFilterUpdated.value?.any { it.selected } == true
                        FilterType.MOVIES -> replySharedViewModel.onMovieFilterUpdated.value?.any { it.selected } == true
                    }

                    setOnMenuItemClickListener {
                        displayFilterDialog(filter.getType()) { selected ->
                            isChecked = selected.isNotEmpty()
                        }
                        true
                    }
                }
            }

            //Reset item
            subMenu.add(
                R.id.menu_filter_type_group,
                0,
                subMenu.size(),
                getString(R.string.reset_filters)
            ).apply {
                isCheckable = false
                setOnMenuItemClickListener {
                    resetFilters()
                    true
                }
            }
        }
        return super.onCreateOptionsMenu(menu)
    }

    private fun initializeSearch() {
        activity_main_reset_search.setOnClickListener {
            activity_main_search.text.clear()
            clearSearchFocus()

        }
        activity_main_search.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                clearSearchFocus()
                return@setOnEditorActionListener true
            }
            false
        }
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

    private fun clearSearchFocus() {
        activity_main_search.clearFocus()
        hideKeyboard(main_activity_root_layout)
    }


    private fun resetFilters() {
        replySharedViewModel.resetFilters()
        invalidateOptionsMenu()
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
            FilterType.MOVIES -> dialogComponent.displayMultiChoiceDialog(
                title = R.string.filter_movies,
                choices = replySharedViewModel.movieFilters.map { it.getDisplayName(this) },
                selected = replySharedViewModel.movieFilters.mapIndexedNotNull { index, character ->
                    if (character.selected) {
                        index
                    } else {
                        null
                    }
                }.toIntArray(),
                positiveText = R.string.confirm,
                onPositiveClick = {
                    onFilterSelected(it)
                    replySharedViewModel.selectMovieFilters(it)
                },
                negativeText = R.string.cancel,
                onNegativeClick = {}
            )
        }
    }

    private fun updateFilterIndicator(filters: List<FilterType>) {
        val filterContent = mutableListOf<String>()
        filters.sorted().forEach {
            when (it) {
                FilterType.CHARACTERS -> {
                    val currentCharacterFilters =
                        replySharedViewModel.characterFilters.filter { it.selected }
                    val charactersFilterText =
                        currentCharacterFilters.take(
                            min(currentCharacterFilters.size, SINGLE_FILTER_DISPLAY_COUNT_LIMIT)
                        ).joinToString { it.getDisplayName(this) }
                    filterContent.add(
                        generateFilterText(
                            currentCharacterFilters.size,
                            charactersFilterText,
                            getString(R.string.filter_characters)
                        )
                    )
                }
                FilterType.MOVIES -> {
                    val currentMovieFilters =
                        replySharedViewModel.movieFilters.filter { it.selected }
                    val moviesFilterText =
                        currentMovieFilters.take(
                            min(currentMovieFilters.size, SINGLE_FILTER_DISPLAY_COUNT_LIMIT)
                        ).joinToString { it.getDisplayName(this) }
                    filterContent.add(
                        generateFilterText(
                            currentMovieFilters.size,
                            moviesFilterText,
                            getString(R.string.filter_movies)
                        )
                    )
                }
            }
        }
        activity_main_filter_group.show()
        activity_main_filter_indicator_layout.removeAllViews()
        filterContent.forEach {
            activity_main_filter_indicator_layout.addView(TextView(this).apply {
                text = it
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.MIDDLE
            })
        }

    }

    private fun generateFilterText(size: Int, text: String, label: String) =
        if (size > SINGLE_FILTER_DISPLAY_COUNT_LIMIT) {
            val content = resources.getQuantityString(
                R.plurals.single_filter_content_limit_format,
                size - SINGLE_FILTER_DISPLAY_COUNT_LIMIT,
                text,
                size - SINGLE_FILTER_DISPLAY_COUNT_LIMIT
            )
            getString(R.string.single_filter_full_content_format, label, content)
        } else {
            getString(R.string.single_filter_full_content_format, label, text)
        }

}
