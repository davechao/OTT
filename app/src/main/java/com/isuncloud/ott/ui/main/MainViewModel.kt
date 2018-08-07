package com.isuncloud.ott.ui

import android.app.Application
import com.google.firebase.firestore.FirebaseFirestore
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.FireStoreRepository
import org.web3j.crypto.ECKeyPair
import org.web3j.crypto.Keys
import org.web3j.utils.Numeric
import timber.log.Timber
import javax.inject.Inject

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    var isClickApp = false

    private lateinit var startDate: Date
    private lateinit var ecKeyPair: ECKeyPair

    private val applicationContext = app.applicationContext

    private val compositeDisposable by lazy { CompositeDisposable() }

    lateinit var appItem: AppItem

    lateinit var androidId: String

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var fireStoreRepository: FireStoreRepository

    @Inject
    lateinit var apiRepository: ApiRepository

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    init {
        OTTApp.getAppComponent().inject(this)
    }

    fun enterApp(item: AppItem) {
        appItem = item
        startDate = Date()
    }

    fun exitApp() {
        val endDate = Date()

        fireStoreRepository.sendFireStoreData(
                startDate,
                endDate,
                appItem,
                androidId,
                ecKeyPair)
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

}