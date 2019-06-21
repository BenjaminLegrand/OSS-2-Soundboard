package fr.legrand.oss117soundboard.presentation.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.legrand.oss117soundboard.presentation.di.annotation.PerService
import fr.legrand.oss117soundboard.presentation.service.BackgroundListenService

@Module
abstract class ServiceInjectorModule {

    @PerService
    @ContributesAndroidInjector
    abstract fun backgroundListenServiceInjector(): BackgroundListenService
}