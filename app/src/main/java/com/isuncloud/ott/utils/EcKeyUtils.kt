package com.isuncloud.ott.utils

import org.web3j.utils.Numeric
import java.math.BigInteger

object EcKeyUtils {

    fun getPrivateKeyToBates(privateKey: BigInteger): ByteArray {
        return Numeric.toBytesPadded(privateKey, 32)
    }

    fun getPublicKeyToBates(publicKey: BigInteger): ByteArray {
        return Numeric.toBytesPadded(publicKey, 64)
    }

    fun getPrivateKeyToString(privateKey: ByteArray): String {
        return Numeric.toHexString(privateKey)
    }

    fun getPublicKeyToString(publicKey: ByteArray): String {
        return Numeric.toHexString(publicKey)
    }

}