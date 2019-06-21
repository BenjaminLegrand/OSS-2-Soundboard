package fr.legrand.oss117soundboard.presentation.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
    @Inject
    lateinit var notificationManager: NotificationManager


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
        val currentNotificationBuilder = NotificationCompat.Builder(this, getChannelId())
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(getString(R.string.background_listen))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setColor(ContextCompat.getColor(this, R.color.colorAccent))
            .addAction(
                R.drawable.ic_notif_stop_listen, getString(R.string.stop_listen),
                PendingIntent.getBroadcast(
                    this,
                    0,
                    Intent(this, BackgroundListenStopReceiver::class.java),
                    0
                )
            )


        disposable.add(contentRepository.listenToPlayerStatus().subscribeBy {
            updateNotificationCount(it.second, currentNotificationBuilder)
            when (it.first) {
                PlayerStatus.BOUND -> {
                    startForeground(NOTIFICATION_ID, currentNotificationBuilder.build())
                }
                PlayerStatus.SINGLE_REPLY_ENDED -> {
                    notificationManager.notify(NOTIFICATION_ID, currentNotificationBuilder.build())
                }

                PlayerStatus.ENDED -> stop()
            }
        })
    }

    private fun updateNotificationCount(count: Int, builder: NotificationCompat.Builder) {
        builder.setContentText(
            resources.getQuantityString(
                R.plurals.background_listen_notif_content,
                count,
                count
            )
        )
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