package com.isuncloud.ott.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.isuncloud.ott.repository.model.AppItem
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.collections.HashMap

@Singleton
class FireStoreRepository @Inject constructor(
        val firestore: FirebaseFirestore) {

    companion object {
        private const val COLLECTION_PATH_OTT = "OTT"
    }

    private var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    lateinit var signDataMap: HashMap<String, Any>

    fun sendFireStoreData(
            startDate: Date,
            endDate: Date,
            appItem: AppItem,
            androidId: String,
            ecKeyPair: ECKeyPair) {

        val duration = (endDate.time - startDate.time) / 1000

        val ratingsMap = hashMapOf<String, Any>()
        ratingsMap["AppSTime"] = sdf.format(startDate)
        ratingsMap["AppETime"] = sdf.format(endDate)
        ratingsMap["AppRunduration"] = duration

        val appDataMap = hashMapOf<String, Any>()
        appDataMap["AppId"] = appItem.appId
        appDataMap["AppName"] = appItem.appName
        appDataMap["Ratings"] = ratingsMap

        val dataJson = Gson().toJson(appDataMap)
        Timber.d("data: " + dataJson)

        val data = dataJson.toByteArray()
        signData(data, ecKeyPair)

        val ottMap = hashMapOf<String, Any>()
        ottMap["DeviceId"] = androidId
        ottMap["AppData"] = appDataMap
        ottMap["SignData"] = signDataMap

        firestore.collection(COLLECTION_PATH_OTT)
                .document()
                .set(ottMap)
                .addOnSuccessListener {
                    Timber.d("data written successfully!")
                }
                .addOnFailureListener {
                    Timber.d("data written fail!")
                }
    }

    fun signData(data: ByteArray, ecKeyPair: ECKeyPair) {
        val hashData = Hash.sha3(data)
        Timber.d("hashData: " + Numeric.toHexString(hashData))

        val signatureData = Sign.signMessage(data, ecKeyPair)
        val r =  Numeric.toHexString(signatureData.r)
        val s = Numeric.toHexString(signatureData.s)
        val v = signatureData.v.toInt()

        Timber.d("r: " + r)
        Timber.d("s: " + s)
        Timber.d("v: " + v)

        val sigMap = hashMapOf<String, Any>()
        sigMap["R"] = r
        sigMap["S"] = s
        sigMap["V"] = v

        signDataMap = hashMapOf()
        signDataMap["HashData"] = Numeric.toHexString(hashData)
        signDataMap["Sig"] = sigMap
    }

}