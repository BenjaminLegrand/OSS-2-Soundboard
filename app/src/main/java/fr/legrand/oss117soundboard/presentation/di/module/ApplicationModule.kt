package fr.legrand.oss117soundboard.presentation.di.module

import android.app.Application
import android.app.NotificationManager
import android.app.job.JobScheduler
import android.content.Context
import dagger.Module
import dagger.Provides
import fr.legrand.oss117soundboard.data.manager.file.FileManager
import fr.legrand.oss117soundboard.data.manager.file.FileManagerImpl
import fr.legrand.oss117soundboard.data.manager.media.MediaPlayerManager
import fr.legrand.oss117soundboard.data.manager.media.MediaPlayerManagerImpl
import fr.legrand.oss117soundboard.data.manager.sharedpref.SharedPrefManager
import fr.legrand.oss117soundboard.data.manager.sharedpref.SharedPrefManagerImpl
import fr.legrand.oss117soundboard.data.manager.storage.StorageManager
import fr.legrand.oss117soundboard.data.manager.storage.StorageManagerImpl
import fr.legrand.oss117soundboard.data.repository.ContentRepository
import fr.legrand.oss117soundboard.data.repository.ContentRepositoryImpl
import fr.legrand.oss117soundboard.presentation.component.background.BackgroundComponent
import fr.legrand.oss117soundboard.presentation.component.background.BackgroundComponentImpl
import javax.inject.Singleton

@Module
class ApplicationModule {

    @Singleton
    @Provides
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun provideContentRepository(repo: ContentRepositoryImpl): ContentRepository = repo

    @Singleton
    @Provides
    fun provideStorageManager(manager: StorageManagerImpl): StorageManager = manager

    @Singleton
    @Provides
    fun provideFileManager(manager: FileManagerImpl): FileManager = manager

    @Singleton
    @Provides
    fun provideShardPrefsManager(manager: SharedPrefManagerImpl): SharedPrefManager =
        manager

    @Singleton
    @Provides
    fun provideMediaManager(mediaPlayerComponentImpl: MediaPlayerManagerImpl): MediaPlayerManager =
        mediaPlayerComponentImpl

    @Singleton
    @Provides
    fun provideBackgroundComponent(backgroundComponentImpl: BackgroundComponentImpl): BackgroundComponent =
        backgroundComponentImpl

    @Singleton
    @Provides
    fun provideNotificationManager(context: Context): NotificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @Singleton
    @Provides
    fun provideJobScheduler(context: Context): JobScheduler =
        context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler

}