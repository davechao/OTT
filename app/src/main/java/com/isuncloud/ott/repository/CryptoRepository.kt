package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.db.dao.EcKeyDao
import com.isuncloud.ott.repository.model.EcKey
import com.isuncloud.ott.repository.model.Sig
import com.isuncloud.ott.repository.model.SignItem
import com.isuncloud.ott.utils.EcKeyUtils
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CryptoRepository @Inject constructor(
        private val ecKeyDao: EcKeyDao) {

    fun isExistEcKeyPair() = ecKeyDao.loadEcKeys().isEmpty()

    fun saveEcKeyPair(privateKey: String) {
        val ecKey = EcKey(
                privateKey = privateKey,
                publicKey = "")
        ecKeyDao.insertEcKey(ecKey)
    }
    
//    fun createEcKeyPair() {
//        val ecKeys = ecKeyDao.loadEcKeys()
//
//        if(ecKeys.isEmpty()) {
//            val ecKeyPair = Keys.createEcKeyPair()
//            val privateKey = ecKeyPair.privateKey
//            val publicKey= ecKeyPair.publicKey
//            val ecKey = EcKey(
//                    privateKey = privateKey.toString(),
//                    publicKey = publicKey.toString())
//
//            val privateKeyBytes = EcKeyUtils.getPrivateKeyToBates(privateKey)
//            val publicKeyBytes = EcKeyUtils.getPublicKeyToBates(publicKey)
//            val privateKeyStr = EcKeyUtils.getPrivateKeyToString(privateKeyBytes)
//            val publicKeyStr = EcKeyUtils.getPublicKeyToString(publicKeyBytes)
//            Timber.d("privateKey: " + privateKeyStr)
//            Timber.d("publicKey: " + publicKeyStr)
//
//            ecKeyDao.insertEcKey(ecKey)
//        }
//    }

    fun signData(data: ByteArray, ecKeyPair: ECKeyPair): SignItem {
        val hashData = Hash.sha3(data)
        Timber.d("hashData: " + Numeric.toHexString(hashData))

        val signatureData = Sign.signMessage(data, ecKeyPair)
        val r =  Numeric.toHexString(signatureData.r)
        val s = Numeric.toHexString(signatureData.s)
        val v = signatureData.v.toInt()

        Timber.d("r: " + r)
        Timber.d("s: " + s)
        Timber.d("v: " + v)

        val sig = Sig(r, s, v)
        return SignItem(hashData, sig)
    }

    fun getEcKeyPair(): ECKeyPair {
        val ecKeys = ecKeyDao.loadEcKeys()

        val privateKey = ecKeys[0].privateKey.toBigInteger()
        val publicKey = ecKeys[0].publicKey.toBigInteger()

        val privateKeyBytes = EcKeyUtils.getPrivateKeyToBates(privateKey)
        val publicKeyBytes = EcKeyUtils.getPublicKeyToBates(publicKey)
        val privateKeyStr = EcKeyUtils.getPrivateKeyToString(privateKeyBytes)
        val publicKeyStr = EcKeyUtils.getPublicKeyToString(publicKeyBytes)
        Timber.d("privateKey: " + privateKeyStr)
        Timber.d("publicKey: " + publicKeyStr)

        return ECKeyPair(privateKey, publicKey)
    }

}