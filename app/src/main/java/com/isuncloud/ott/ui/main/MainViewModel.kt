package com.isuncloud.ott.ui.main

import android.app.Application
import android.text.TextUtils
import com.facebook.react.ReactInstanceManager
import com.google.gson.Gson
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.repository.model.AppItem
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import java.util.*
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.repository.AppRepository
import com.isuncloud.ott.repository.model.firestore.AppData
import com.isuncloud.ott.repository.model.firestore.Ratings
import com.isuncloud.ott.repository.model.rn.*
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

//    var isClickApp = false

    lateinit var wizardModule: WizardModule

    @Inject lateinit var reactInstanceManager: ReactInstanceManager
    @Inject lateinit var schedulerProvider: SchedulerProvider

//    @Inject lateinit var firestore: FirebaseFirestore
//    @Inject lateinit var fireStoreRepository: FireStoreRepository
//    @Inject lateinit var apiRepository: ApiRepository
//    @Inject lateinit var cryptoRepository: CryptoRepository

    @Inject lateinit var appRepository: AppRepository

    @Inject lateinit var gson: Gson

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    init {
        OTTApp.getAppComponent().inject(this)
        setupReactInstanceManagerListener()
    }

//    fun enterApp(item: AppItem) {
//        appItem = item
//        startDate = Date()
//    }

//    fun exitApp() {
//        val endDate = Date()
//        fireStoreRepository.sendDataToFireStore(
//                startDate,
//                endDate,
//                appItem,
//                androidId)
//    }

//    fun createEcKeyPair() {
//        cryptoRepository.createEcKeyPair()
//    }

    private fun setupReactInstanceManagerListener() {
        reactInstanceManager.addReactInstanceEventListener {
            wizardModule = it.getNativeModule(WizardModule::class.java)
            OTTApp.getAppComponent().inject(wizardModule)
            Timber.d("RN is started")
            doInitInfiniteChain()
        }
    }

    private fun doInitInfiniteChain() {

        var primaryKey = appRepository.getPrivateKey()
        if(TextUtils.isEmpty(primaryKey)) {
            primaryKey = null
        }

        val env = EnvRequest(
                serverUrl = BuildConfig.ServerUrl,
                nodeUrl = BuildConfig.NodeUrl,
                web3Url = BuildConfig.Web3Url,
                privateKey = "41b1a0649752af1b28b3dc29a1556eee781e4a4c3a1f7f53f90fa834de098c41",
                storage = "level")
        compositeDisposable.add(
                wizardModule.initInfiniteChain(env)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d(it)
                                    val initResult = gson.fromJson(it, InitResult::class.java)
                                    val privateKey = initResult.privateKey
                                    val address = initResult.address
                                    appRepository.saveEcKeyPair(privateKey)
                                    appRepository.saveWallet(address)
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }
                        )
        )
    }

    fun makeLightTx() {

        val ratings = Ratings(
                appSTime = "2018/08/10 09:52:20",
                appETime = "2018/08/10 09:52:25",
                appRunduration = 5
        )

        val appData = AppData(
                appId = "com.android.chrome",
                appName = "Chrome",
                ratings = ratings
        )

        val client = Client(
                appData = appData,
                deviceId = "c45a632b90d3f886"
        )

        val metaDataModel = MetaDataModel(
                client = client
        )

        val lightTx = MakeLightTx(
                fromAddress = "e422277c7333020f8dd254b7e8bdfb63c83465be",
                toAddress = "9644fb7d0108a6b7e52cab5171298969a427cacd",
                metadata = metaDataModel
        )

        compositeDisposable.add(
                wizardModule.makeLightTx(lightTx)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("makeLightTx: " + it)
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }
                        )
        )
    }

}