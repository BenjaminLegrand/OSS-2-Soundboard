package fr.legrand.oss117soundboard.presentation.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.android.AndroidInjection
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.data.values.PlayerStatus
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

private const val NOTIFICATION_ID = 1054
private const val CHANNEL_ID = "BACKGROUND_LISTEN_CHANNEL_ID"
private const val CHANNEL_NAME = "BACKGROUND_LISTEN_CHANNEL"

class BackgroundListenService : JobService() {

    @Inject
    lateinit var contentRepository: ContentRepository

    private val disposable = CompositeDisposable()
    private var parameters: JobParameters? = null

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onStartJob(p0: JobParameters?): Boolean {
        parameters = p0
        checkPlayerRunning()
        return true
    }

    override fun onStopJob(p0: JobParameters?): Boolean {
        stop()
        return false
    }

    private fun checkPlayerRunning() {
        disposable.add(contentRepository.isPlayerRunning().subscribeBy {
            if (it) {
                startForeground()
            } else {
                stop()
            }
        })
    }

    private fun startForeground() {
        startForeground(
            NOTIFICATION_ID, NotificationCompat.Builder(this, getChannelId())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(
                    String.format(
                        getString(R.string.background_listen_notif_format),
                        getString(R.string.app_name),
                        getString(R.string.background_listen).toLowerCase()
                    )
                )
                .setContentText(getString(R.string.background_listen_notif_content))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(Notification.CATEGORY_SERVICE).build()
        )

        disposable.add(contentRepository.listenToPlayerStatus().subscribeBy {
            when (it.first) {
                PlayerStatus.SINGLE_REPLY_ENDED -> Log.i("TAG", "REMAINING ${it.second}")
                PlayerStatus.ENDED -> stop()
                PlayerStatus.STARTED -> {
                    /*Nothing to do currently*/
                }
            }
        })
    }

    private fun getChannelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                )
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return CHANNEL_ID
    }

    private fun stop() {
        disposable.clear()
        parameters?.let {
            jobFinished(parameters, false)
        }
        stopForeground(true)
        stopSelf()
    }

    companion object {
        const val JOB_ID = 2567
    }

}