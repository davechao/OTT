package com.isuncloud.ott.di.module

import android.app.Application
import android.arch.persistence.room.Room
import com.facebook.react.ReactInstanceManager
import com.facebook.react.common.LifecycleState
import com.facebook.react.shell.MainReactPackage
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.app.EventPublishSubject
import com.isuncloud.ott.app.Pref
import com.isuncloud.ott.database.AppDatabase
import com.isuncloud.ott.event.BaseRxEvent
import com.isuncloud.ott.reactnative.module.WizardPackage
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create()
    }

    @Provides
    @Singleton
    fun providePref(gson: Gson, application: Application): Pref {
        return Pref(gson, application.applicationContext, "OTT_Prefs")
    }

    @Provides
    @Singleton
    fun provideEventPublishSubject(): EventPublishSubject<BaseRxEvent> {
        return EventPublishSubject()
    }

    @Provides
    @Singleton
    fun provideDatabase(application: Application): AppDatabase {
        val builder = Room.databaseBuilder(application.applicationContext, AppDatabase::class.java, "OTT")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
        return builder.build()
    }

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun providesReactNative(application: Application): ReactInstanceManager {
        return ReactInstanceManager.builder()
                .setApplication(application)
                .setBundleAssetName("wizard_mobile.bundle")
                .addPackage(MainReactPackage())
                .addPackage(WizardPackage())
                .setUseDeveloperSupport(BuildConfig.DEBUG)
                .setInitialLifecycleState(LifecycleState.RESUMED)
                .build()
    }

}