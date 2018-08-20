package com.isuncloud.ott.ui.main

import android.app.Application
import com.facebook.react.ReactInstanceManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.CryptoRepository
import com.isuncloud.ott.repository.FireStoreRepository
import com.isuncloud.ott.repository.model.rn.EnvRequest
import com.isuncloud.ott.repository.model.rn.InitResult
import com.isuncloud.ott.rn.WizardModule
import com.isuncloud.ott.utils.SchedulerProvider
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    private val applicationContext = app.applicationContext

    private val compositeDisposable by lazy { CompositeDisposable() }

    lateinit var androidId: String
    private lateinit var startDate: Date
    private lateinit var appItem: AppItem

    var isClickApp = false

    lateinit var wizardModule: WizardModule

    @Inject lateinit var reactInstanceManager: ReactInstanceManager
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var firestore: FirebaseFirestore
    @Inject lateinit var fireStoreRepository: FireStoreRepository
    @Inject lateinit var cryptoRepository: CryptoRepository
    @Inject lateinit var apiRepository: ApiRepository
    @Inject lateinit var gson: Gson

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

        compositeDisposable.add(
                wizardModule.initInfiniteChain(env)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    val initResult = gson.fromJson(it, InitResult::class.java)
                                    Timber.d(initResult.toString())
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }
                        )
        )
    }

}