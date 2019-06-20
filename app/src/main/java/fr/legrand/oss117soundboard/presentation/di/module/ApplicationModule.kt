package fr.legrand.oss117soundboard.presentation.di.module

import android.app.Application
import android.app.job.JobScheduler
import android.content.Context
import dagger.Binds
import dagger.Module
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
abstract class ApplicationModule {

    @Singleton
    @Binds
    abstract fun provideContext(application: Application): Context

    @Singleton
    @Binds
    abstract fun provideContentRepository(repo: ContentRepositoryImpl): ContentRepository

    @Singleton
    @Binds
    abstract fun provideStorageManager(manager: StorageManagerImpl): StorageManager

    @Singleton
    @Binds
    abstract fun provideFileManager(manager: FileManagerImpl): FileManager

    @Singleton
    @Binds
    abstract fun provideShardPrefsManager(manager: SharedPrefManagerImpl): SharedPrefManager

    @Singleton
    @Binds
    abstract fun provideMediaManager(mediaPlayerComponentImpl: MediaPlayerManagerImpl): MediaPlayerManager

    @Singleton
    @Binds
    abstract fun provideBackgroundComponent(backgroundComponentImpl: BackgroundComponentImpl): BackgroundComponent

}