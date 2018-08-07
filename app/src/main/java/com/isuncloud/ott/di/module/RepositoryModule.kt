package com.isuncloud.ott.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.CryptoRepository
import com.isuncloud.ott.repository.FireStoreRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun providesCryptoRepository() = CryptoRepository()

    @Provides
    @Singleton
    fun providesFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    fun provideFireStoreRepository(firestore: FirebaseFirestore, cryptoRepository: CryptoRepository)
            = FireStoreRepository(firestore, cryptoRepository)

    @Provides
    @Singleton
    fun provideApiRepository() = ApiRepository()
}