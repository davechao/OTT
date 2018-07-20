package com.isuncloud.ott.ui

import com.isuncloud.ott.ui.main.MainFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentProvider {

    @ContributesAndroidInjector
    abstract fun provideMainFragmentFactory(): MainFragment

}