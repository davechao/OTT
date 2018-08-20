package com.isuncloud.ott.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.isuncloud.ott.database.AppDatabase
import com.isuncloud.ott.repository.ApiService
import com.isuncloud.ott.repository.AppRepository
import com.isuncloud.ott.repository.CryptoRepository
import com.isuncloud.ott.repository.FireStoreRepository
import com.isuncloud.ott.repository.db.dao.EcKeyDao
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

    companion object {
        const val API_BASE_URL = "http://192.168.2.83:3000"
    }

    @Provides
    @Singleton
    fun provideEcKeyDao(appDatabase: AppDatabase): EcKeyDao {
        return appDatabase.ecKeyDao()
    }

    @Provides
    @Singleton
    fun provideWalletDao(appDatabase: AppDatabase): WalletDao {
        return appDatabase.walletDao()
    }

    @Provides
    @Singleton
    fun providesCryptoRepository(ecKeyDao: EcKeyDao) = CryptoRepository(ecKeyDao)

    @Provides
    @Singleton
    fun provideFireStoreRepository(firestore: FirebaseFirestore, cryptoRepository: CryptoRepository)
            = FireStoreRepository(firestore, cryptoRepository)

    @Provides
    @Singleton
    fun providesAppRepository(ecKeyDao: EcKeyDao, walletDao: WalletDao) = AppRepository(ecKeyDao, walletDao)

    @Provides
    @Singleton
    fun provideApiService(gson: Gson, okHttpClient: OkHttpClient): ApiService {
        return Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(okHttpClient)
                .baseUrl(API_BASE_URL)
                .build().create(ApiService::class.java)
    }

}