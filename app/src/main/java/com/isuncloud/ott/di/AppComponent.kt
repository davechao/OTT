package com.isuncloud.ott.di

import android.app.Application
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.di.module.AppModule
import com.isuncloud.ott.di.module.NetworkModule
import com.isuncloud.ott.di.module.RepositoryModule
import com.isuncloud.ott.rn.WizardModule
import com.isuncloud.ott.ui.main.MainViewModel
import dagger.BindsInstance
import dagger.Component
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (AndroidSupportInjectionModule::class),
    (AppModule::class),
    (NetworkModule::class),
    (RepositoryModule::class)])
interface AppComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(app: Application): Builder
        fun build(): AppComponent
    }

    fun inject(app: OTTApp)
    fun inject(mainViewModel: MainViewModel)
    fun inject(wizardModule: WizardModule)
}