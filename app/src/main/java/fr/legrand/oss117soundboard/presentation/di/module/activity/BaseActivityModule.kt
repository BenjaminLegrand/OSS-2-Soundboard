package fr.legrand.oss117soundboard.presentation.di.module.activity

import androidx.fragment.app.FragmentManager
import dagger.Module
import dagger.Provides
import fr.legrand.oss117soundboard.presentation.component.dialog.DialogComponent
import fr.legrand.oss117soundboard.presentation.component.dialog.MaterialDialogComponent
import fr.legrand.oss117soundboard.presentation.di.annotation.PerActivity
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity


@Module
class BaseActivityModule {

    @PerActivity
    @Provides
    fun provideSupportFragmentManager(activity: BaseActivity): FragmentManager = activity.supportFragmentManager

    @PerActivity
    @Provides
    fun provideDialogComponent(materialDialogComponent: MaterialDialogComponent): DialogComponent =
        materialDialogComponent
}