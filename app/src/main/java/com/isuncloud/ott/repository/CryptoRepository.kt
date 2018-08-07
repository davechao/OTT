package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.model.Sig
import com.isuncloud.ott.repository.model.SignItem
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import timber.log.Timber
import javax.inject.Singleton

@Singleton
class CryptoRepository {

    fun createEcKeyPair() {
        val ecKeyPair = Keys.createEcKeyPair()
        val privateKey = ecKeyPair.privateKey
        val publicKey= ecKeyPair.publicKey

        val privateKeyBytes = Numeric.toBytesPadded(privateKey, 32)
        val publicKeyBytes = Numeric.toBytesPadded(publicKey, 64)
        val privateKeyStr = Numeric.toHexString(privateKeyBytes)
        val publicKeyStr = Numeric.toHexString(publicKeyBytes)

        Timber.d("PrivateKey: " + privateKeyStr)
        Timber.d("PublicKey: " + publicKeyStr)
    }

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
        val signItem = SignItem(hashData, sig)

        return signItem
    }

}