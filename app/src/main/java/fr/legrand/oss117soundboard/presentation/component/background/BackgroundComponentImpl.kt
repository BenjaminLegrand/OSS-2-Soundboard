package fr.legrand.oss117soundboard.presentation.component.background

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import fr.legrand.oss117soundboard.presentation.service.BackgroundListenService
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class BackgroundComponentImpl @Inject constructor(private val context: Context) :
    BackgroundComponent {

    private val jobScheduler = context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
    private val appStateSubject = PublishSubject.create<AppState>()

    override fun startBackgroundListenService() {
        val jobInfo =
            JobInfo.Builder(BackgroundListenService.JOB_ID, ComponentName(context, BackgroundListenService::class.java))
                .setMinimumLatency(1)
                .setOverrideDeadline(1)
                .build()
        jobScheduler.schedule(jobInfo)
    }

    override fun stopBackgroundListenService() {
        jobScheduler.cancel(BackgroundListenService.JOB_ID)
    }

    override fun onForeground() {
        appStateSubject.onNext(AppState.FOREGROUND)
    }

    override fun onBackground() {
        appStateSubject.onNext(AppState.BACKGROUND)
    }

    override fun listenToAppState(): Observable<AppState> = appStateSubject

}