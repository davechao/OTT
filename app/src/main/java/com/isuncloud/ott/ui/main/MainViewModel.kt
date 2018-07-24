package com.isuncloud.ott.ui

import android.app.Application
import android.os.Build
import com.google.firebase.firestore.FirebaseFirestore
import com.isuncloud.isuntvmall.database.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import org.threeten.bp.LocalDateTime
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Hash
import org.web3j.crypto.Keys
import org.web3j.crypto.Sign
import org.web3j.utils.Numeric
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import com.google.gson.Gson
import kotlin.collections.HashMap

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    companion object {
        private const val COLLECTION_PATH_OTT = "OTT"
    }

    private var sdf = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")

    var isClickApp = false

    private lateinit var startDate: Date
    private lateinit var ecKeyPair: ECKeyPair

    private val applicationContext = app.applicationContext

    private val compositeDisposable by lazy { CompositeDisposable() }

    lateinit var db: FirebaseFirestore

    lateinit var appItem: AppItem

    lateinit var signDataMap: HashMap<String, Any>

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    init {
        setupFirestore()
    }

    private fun setupFirestore() {
        db = FirebaseFirestore.getInstance()
    }

    fun enterApp(item: AppItem) {
        appItem = item
        startDate = Date()
    }

    fun exitApp() {
        val endDate = Date()
        val duration = (endDate.time - startDate.time) / 1000
        val createTimestamp = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())

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
        signData(data)

        val ottMap = hashMapOf<String, Any>()
        ottMap["DeviceId"] = Build.SERIAL
        ottMap["AppData"] = appDataMap
        ottMap["SignData"] = signDataMap
        ottMap["CreateTimestamp"] = createTimestamp.toString()

        db.collection(COLLECTION_PATH_OTT)
                .document()
                .set(ottMap)
                .addOnSuccessListener {
                    Timber.d("data written successfully!")
                }
                .addOnFailureListener {
                    Timber.d("data written fail!")
                }
    }

    fun createEcKeyPair() {
        ecKeyPair = Keys.createEcKeyPair()
        val privateKey = ecKeyPair.privateKey
        val publicKey= ecKeyPair.publicKey

        val privateKeyBytes = Numeric.toBytesPadded(privateKey, 32)
        val publicKeyBytes = Numeric.toBytesPadded(publicKey, 64)
        val privateKeyStr = Numeric.toHexString(privateKeyBytes)
        val publicKeyStr = Numeric.toHexString(publicKeyBytes)

        Timber.d("PrivateKey: " + privateKeyStr)
        Timber.d("PublicKey: " + publicKeyStr)
    }

    fun signData(data: ByteArray) {
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