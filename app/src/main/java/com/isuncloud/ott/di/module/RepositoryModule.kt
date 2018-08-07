package com.isuncloud.ott.di.module

import com.google.firebase.firestore.FirebaseFirestore
import com.isuncloud.ott.database.AppDatabase
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.CryptoRepository
import com.isuncloud.ott.repository.FireStoreRepository
import com.isuncloud.ott.repository.db.dao.EcKeyDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideEcKeyDao(appDatabase: AppDatabase): EcKeyDao {
        return appDatabase.ecKeyDao()
    }

    @Provides
    @Singleton
    fun providesCryptoRepository(ecKeyDao: EcKeyDao) = CryptoRepository(ecKeyDao)

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