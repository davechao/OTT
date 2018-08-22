package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.db.dao.WalletDao
import com.isuncloud.ott.repository.model.EcKey
import com.isuncloud.ott.repository.model.Wallet
import javax.inject.Inject

class AppRepository @Inject constructor(
        private val ecKeyDao: EcKeyDao,
        private val walletDao: WalletDao) {


    fun saveEcKeyPair(privateKey: String) {
        val ecKey = EcKey(
                privateKey = privateKey,
                publicKey = "")
        ecKeyDao.insertEcKey(ecKey)
    }

    fun getPrivateKey(): String? {
        var privateKey = ""
        val ecKeys = ecKeyDao.loadEcKeys()
        if(!ecKeys.isEmpty()) {
            privateKey = ecKeys[0].privateKey
        }
        return privateKey
    }

    fun saveWallet(address: String) {
        val wallet = Wallet(address = address)
        walletDao.insertWallet(wallet)
    }


}