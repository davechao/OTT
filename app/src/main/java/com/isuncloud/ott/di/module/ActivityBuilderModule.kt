package com.isuncloud.ott.di.module

import com.isuncloud.ott.ui.MainFragmentProvider
import com.isuncloud.ott.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [MainFragmentProvider::class])
    abstract fun bindMainActivity(): MainActivity

}