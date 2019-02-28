package fr.legrand.oss117soundboard.presentation.navigator

import android.content.Intent
import android.net.Uri
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import javax.inject.Inject

/**
 * Created by Benjamin on 30/09/2017.
 */
class MainNavigator @Inject
constructor(private val baseActivity: BaseActivity)  {

    fun launchBrowsingApp() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(baseActivity.getString(R.string.easter_egg_url)))
        baseActivity.startActivity(browserIntent)
    }

}
