package fr.legrand.oss117soundboard.presentation.di.module

import androidx.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import fr.legrand.oss117soundboard.presentation.di.annotation.ViewModelKey
import fr.legrand.oss117soundboard.presentation.ui.main.MainViewModel
import fr.legrand.oss117soundboard.presentation.ui.main.ReplySharedViewModel
import fr.legrand.oss117soundboard.presentation.ui.reply.ReplyListViewModel
import fr.legrand.oss117soundboard.presentation.ui.settings.SettingsViewModel

/**
 * Created by Benjamin on 21/04/2018.
 */
@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(ReplyListViewModel::class)
    abstract fun bindReplyListViewModel(viewModel: ReplyListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    abstract fun bindMainViewModel(viewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ReplySharedViewModel::class)
    abstract fun bindSharedViewModel(viewModel: ReplySharedViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingsViewModel::class)
    abstract fun bindSettingsViewModel(viewModel: SettingsViewModel): ViewModel

}