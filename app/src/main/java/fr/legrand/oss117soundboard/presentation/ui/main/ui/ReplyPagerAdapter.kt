package fr.legrand.oss117soundboard.presentation.ui.main.ui


import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import fr.legrand.oss117soundboard.presentation.ui.reply.ReplyListFragment
import fr.legrand.oss117soundboard.presentation.ui.settings.SettingsFragment
import javax.inject.Inject

/**
 * Created by Benjamin on 12/10/2017.
 */

private const val FAVORITE_LIST_POSITION = 0
private const val REPLY_LIST_POSITION = 1
private const val PARAMETER_POSITION = 2
private const val FRAGMENT_COUNT = 3

class ReplyPagerAdapter @Inject
constructor(context: Context, activity: BaseActivity) : FragmentStatePagerAdapter(activity.supportFragmentManager) {
    private var currentPosition: Int = 0

    private val pagerTitles = listOf(
        context.getString(R.string.favorite_list_title),
        context.getString(R.string.reply_list_title),
        context.getString(R.string.parameter_title)
    )

    override fun getItem(position: Int): Fragment? {
        //No need to handle removal from list for now because all fragments are instantiated at the beginning
        return when (position) {
            FAVORITE_LIST_POSITION -> {
                ReplyListFragment.newInstance(true)
            }
            REPLY_LIST_POSITION -> {
                ReplyListFragment.newInstance(false)
            }
            PARAMETER_POSITION -> {
                SettingsFragment.newInstance()
            }
            else -> null
        }
    }

    override fun getCount(): Int {
        return FRAGMENT_COUNT
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return pagerTitles[position]
    }

    fun setCurrentPosition(currentPosition: Int) {
        this.currentPosition = currentPosition
    }

}
