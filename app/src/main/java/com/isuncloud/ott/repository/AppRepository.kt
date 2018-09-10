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
        val ecKey = EcKey(privateKey = privateKey)
        ecKeyDao.insertEcKey(ecKey)
    }

    fun getPrivateKey(): String? {
        var privateKey = ""
        val ecKeys = ecKeyDao.loadEcKeys()
        if(ecKeys.isNotEmpty()) {
            privateKey = ecKeys[0].privateKey
        }
        return privateKey
    }

    fun saveWallet(address: String) {
        val sb = StringBuilder().append("0x").append(address)
        val wallet = Wallet(address = sb.toString())
        walletDao.insertWallet(wallet)
    }

    fun getWallet(): Wallet {
        val wallets = walletDao.loadWallets()
        return  wallets[0]
    }
}