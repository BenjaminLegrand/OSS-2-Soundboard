package fr.legrand.oss117soundboard.presentation.di.module.activity

import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector
import fr.legrand.oss117soundboard.presentation.di.annotation.PerActivity
import fr.legrand.oss117soundboard.presentation.di.annotation.PerFragment
import fr.legrand.oss117soundboard.presentation.ui.base.BaseActivity
import fr.legrand.oss117soundboard.presentation.ui.main.MainActivity
import fr.legrand.oss117soundboard.presentation.ui.reply.ReplyListFragment
import fr.legrand.oss117soundboard.presentation.ui.settings.SettingsFragment

@Module(includes = [BaseActivityModule::class])
abstract class MainActivityModule {
    @PerActivity
    @Binds
    abstract fun bindBaseActivity(mainActivity: MainActivity): BaseActivity

    @PerFragment
    @ContributesAndroidInjector
    abstract fun replyListFragmentInjector(): ReplyListFragment

    @PerFragment
    @ContributesAndroidInjector
    abstract fun settingsFragmentInjector(): SettingsFragment

}