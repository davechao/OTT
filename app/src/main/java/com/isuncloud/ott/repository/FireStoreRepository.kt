package com.isuncloud.ott.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.isuncloud.ott.repository.model.AppItem
import org.web3j.utils.Numeric
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FireStoreRepository @Inject constructor(
        private val firestore: FirebaseFirestore,
        private val cryptoRepository: CryptoRepository) {

    companion object {
        private const val COLLECTION_PATH_OTT = "OTT"
    }

    private var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    fun sendDataToFireStore(
            startDate: Date,
            endDate: Date,
            appItem: AppItem,
            androidId: String) {

        val ecKeyPair = cryptoRepository.getEcKeyPair()

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
        val signItem = cryptoRepository.signData(data, ecKeyPair)
        val hashData = signItem.hashData
        val sig = signItem.sig

        val sigMap = hashMapOf<String, Any>()
        sigMap["R"] = sig.r
        sigMap["S"] = sig.s
        sigMap["V"] = sig.v

        val signDataMap = hashMapOf<String, Any>()
        signDataMap["HashData"] = Numeric.toHexString(hashData)
        signDataMap["Sig"] = sigMap

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

}