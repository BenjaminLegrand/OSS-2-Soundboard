package fr.legrand.oss117soundboard.presentation.navigator

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import javax.inject.Inject


/**
 * Created by Benjamin on 30/09/2017.
 */

private const val PACKAGE_URI_FORMAT = "package:%s"

class MainNavigator @Inject
constructor(private val baseActivity: BaseActivity) {

    fun launchBrowsingApp() {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(baseActivity.getString(R.string.easter_egg_url)))
        baseActivity.startActivity(browserIntent)
    }

    fun shareReply(fileUri: Uri, content: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.type = "audio/*"
        share.putExtra(Intent.EXTRA_TEXT, content)
        share.putExtra(Intent.EXTRA_STREAM, fileUri)
        baseActivity.startActivity(Intent.createChooser(share, baseActivity.getString(R.string.share_title)))
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun requestStartSettingsWritePermission() {
        val intent = Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS).apply {
            data = Uri.parse(PACKAGE_URI_FORMAT.format(baseActivity.packageName))
        }
        baseActivity.startActivity(intent)
    }

}
