package fr.legrand.oss117soundboard.presentation.di.module

import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.legrand.oss117soundboard.presentation.di.annotation.PerActivity
import fr.legrand.oss117soundboard.presentation.di.module.activity.MainActivityModule
import fr.legrand.oss117soundboard.presentation.ui.main.MainActivity

@Module
abstract class ActivityInjectorModule {

    @PerActivity
    @ContributesAndroidInjector(modules = [MainActivityModule::class])
    abstract fun mainActivityInjector(): MainActivity
}