package fr.legrand.oss117soundboard.presentation.ui.main

import android.media.AudioManager
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.navigator.MainNavigator
import fr.legrand.oss117soundboard.presentation.ui.base.BaseVMActivity
import fr.legrand.oss117soundboard.presentation.ui.base.hideKeyboard
import fr.legrand.oss117soundboard.presentation.ui.main.ui.ReplyPagerAdapter
import fr.legrand.oss117soundboard.presentation.utils.StringUtils
import fr.legrand.oss117soundboard.presentation.utils.hide
import fr.legrand.oss117soundboard.presentation.utils.observeSafe
import fr.legrand.oss117soundboard.presentation.utils.show
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */


private const val OFFSCREEN_PAGE_LOADED_NUMBER = 3

class MainActivity : BaseVMActivity<MainViewModel>() {

    override val viewModelClass = MainViewModel::class

    private lateinit var replySharedViewModel: ReplySharedViewModel

    @Inject
    lateinit var mainNavigator: MainNavigator
    @Inject
    lateinit var replyPagerAdapter: ReplyPagerAdapter

    override fun getLayoutId() = R.layout.activity_main

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //We change the media volume affected by hardware buttons
        volumeControlStream = AudioManager.STREAM_MUSIC

        setSupportActionBar(activity_main_toolbar)

        replySharedViewModel = ViewModelProviders.of(this, viewModelFactory)[ReplySharedViewModel::class.java]

        viewModel.replyLoaded.observeSafe(this) {
            reply_view_pager.show()
            main_tab_layout.show()
            initViewPager()
        }

        initializeSearch()

    }

    override fun onPause() {
        super.onPause()
        viewModel.releaseRunningPlayers()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
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
            R.id.menu_stop_listen -> viewModel.releaseRunningPlayers()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun initViewPager() {
        reply_view_pager.adapter = replyPagerAdapter
        reply_view_pager.offscreenPageLimit = OFFSCREEN_PAGE_LOADED_NUMBER
        main_tab_layout.setupWithViewPager(reply_view_pager)
        reply_view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

            override fun onPageSelected(position: Int) {
                replyPagerAdapter.setCurrentPosition(position)
            }

            override fun onPageScrollStateChanged(state: Int) {}
        })

        main_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {}

            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(p0: TabLayout.Tab) {
                replyPagerAdapter.setCurrentPosition(p0.position)
            }

        })
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
}
