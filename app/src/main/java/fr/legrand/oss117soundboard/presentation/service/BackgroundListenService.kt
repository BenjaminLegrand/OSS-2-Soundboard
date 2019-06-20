package fr.legrand.oss117soundboard.presentation.service

import android.app.IntentService
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import dagger.android.AndroidInjection
import fr.legrand.oss117soundboard.R
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import io.reactivex.rxkotlin.subscribeBy
import javax.inject.Inject

private const val NOTIFICATION_ID = 1054
private const val CHANNEL_ID = "BACKGROUND_LISTEN_CHANNEL_ID"
private const val CHANNEL_NAME = "BACKGROUND_LISTEN_CHANNEL"
private const val SERVICE_NAME = "BACKGROUND_LISTEN_SERVICE_NAME"

class BackgroundListenService : IntentService(SERVICE_NAME) {

    @Inject
    lateinit var contentRepository: ContentRepository

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onHandleIntent(intent: Intent?) {
        checkPlayerRunning()
    }

    private fun checkPlayerRunning() {
        contentRepository.isPlayerRunning().subscribeBy {
            if (it) {
                startForeground(
                    NOTIFICATION_ID, NotificationCompat.Builder(this, getChannelId())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Listen")
                        .setContentText("Listen in background")
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setCategory(Notification.CATEGORY_SERVICE).build()
                )
                while (true) {
                    Thread.sleep(100)
                    contentRepository.isPlayerRunning().subscribeBy {
                        if (!it) {
                            Log.i("TAG", "STOP")
                            stopForeground(true)
                            stopSelf()
                        }
                    }
                }
            }
        }
    }

    private fun getChannelId(): String {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH
                ).apply {
                    description = "Desc"
                }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        return CHANNEL_ID
    }


    companion object {
        const val JOB_ID = 1000
    }
}