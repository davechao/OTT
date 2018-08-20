package com.isuncloud.ott.ui

import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.rn.module.WizardModule
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.CryptoRepository
import com.isuncloud.ott.repository.FireStoreRepository
import com.isuncloud.ott.repository.model.rn.EnvRequest
import javax.inject.Inject

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    private val applicationContext = app.applicationContext

    private val compositeDisposable by lazy { CompositeDisposable() }

    lateinit var androidId: String
    private lateinit var startDate: Date
    private lateinit var appItem: AppItem

    var isClickApp = false

    lateinit var wizardModule: WizardModule

    @Inject
    lateinit var reactInstanceManager: ReactInstanceManager

    @Inject
    lateinit var firestore: FirebaseFirestore

    @Inject
    lateinit var fireStoreRepository: FireStoreRepository

    @Inject
    lateinit var cryptoRepository: CryptoRepository

    @Inject
    lateinit var apiRepository: ApiRepository

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    init {
        OTTApp.getAppComponent().inject(this)
        setupReactInstanceManagerListener()
    }

    fun enterApp(item: AppItem) {
        appItem = item
        startDate = Date()
    }

    fun exitApp() {
        val endDate = Date()
        fireStoreRepository.sendDataToFireStore(
                startDate,
                endDate,
                appItem,
                androidId)
    }

    fun createEcKeyPair() {
        cryptoRepository.createEcKeyPair()
    }

    private fun setupReactInstanceManagerListener() {
        reactInstanceManager.addReactInstanceEventListener {
            wizardModule = it.getNativeModule(WizardModule::class.java)
            OTTApp.getAppComponent().inject(wizardModule)
            doInitInfiniteChain()
        }
    }

    private fun doInitInfiniteChain() {
        val env = EnvRequest(
                serverUrl = BuildConfig.ServerUrl,
                nodeUrl = BuildConfig.NodeUrl,
                web3Url = BuildConfig.Web3Url,
                privateKey = "",
                storage = "level")
        wizardModule.initInfiniteChain(env)
    }

}