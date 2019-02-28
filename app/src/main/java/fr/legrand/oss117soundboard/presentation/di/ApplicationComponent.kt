package fr.legrand.oss117soundboard.presentation.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import fr.legrand.oss117soundboard.presentation.OSSApplication
import fr.legrand.oss117soundboard.presentation.di.module.ActivityInjectorModule
import fr.legrand.oss117soundboard.presentation.di.module.ApplicationModule
import fr.legrand.oss117soundboard.presentation.di.module.ViewModelModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    ActivityInjectorModule::class,
    ViewModelModule::class
])
interface ApplicationComponent {

    fun inject(application: OSSApplication)

    @Component.Builder
    abstract class Builder {
        @BindsInstance
        abstract fun application(application: Application): Builder

        abstract fun build(): ApplicationComponent
    }

}