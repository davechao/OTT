package com.isuncloud.ott.di.module

import com.google.gson.Gson
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.repository.ApiService
import com.isuncloud.ott.repository.AppRepository
import com.isuncloud.ott.repository.db.AppDatabase
import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.db.dao.PacketDao
import com.isuncloud.ott.repository.db.dao.WalletDao
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideEcKeyDao(appDatabase: AppDatabase) = appDatabase.ecKeyDao()

    @Provides
    @Singleton
    fun provideWalletDao(appDatabase: AppDatabase) = appDatabase.walletDao()

    @Provides
    @Singleton
    fun providePacketDao(appDatabase: AppDatabase) = appDatabase.packetDao()

    @Provides
    @Singleton
    fun providesAppRepository(ecKeyDao: EcKeyDao,
                              walletDao: WalletDao,
                              packetDao: PacketDao)
            = AppRepository(ecKeyDao, walletDao, packetDao)

    @Provides
    @Singleton
    fun provideApiService(gson: Gson, okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(BuildConfig.ApiUrl)
                .build().create(ApiService::class.java)
    }

}