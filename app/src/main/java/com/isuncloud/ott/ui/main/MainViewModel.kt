package com.isuncloud.ott.ui.main

import android.app.Application
import android.arch.lifecycle.MutableLiveData
import android.text.TextUtils
import com.facebook.react.ReactInstanceManager
import com.google.gson.Gson
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.AppRepository
import com.isuncloud.ott.repository.db.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.model.AppData
import com.isuncloud.ott.repository.model.Ratings
import com.isuncloud.ott.repository.model.Wallet
import com.isuncloud.ott.repository.model.api.*
import com.isuncloud.ott.repository.model.rn.*
import com.isuncloud.ott.rn.WizardModule
import com.isuncloud.ott.utils.SchedulerProvider
import io.reactivex.rxkotlin.subscribeBy
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    private val compositeDisposable by lazy { CompositeDisposable() }

    private lateinit var wizardModule: WizardModule
    private lateinit var wallet: Wallet
    private lateinit var appData: AppData
    private lateinit var ratings: Ratings

    lateinit var deviceId: String
    var uuid: String = ""
    var isClickApp = false
    var isInitRn = false
    var lightTxJson = MutableLiveData<String>()

    @Inject lateinit var reactInstanceManager: ReactInstanceManager
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var appRepository: AppRepository
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

    private fun setupReactInstanceManagerListener() {
        reactInstanceManager.addReactInstanceEventListener {
            wizardModule = it.getNativeModule(WizardModule::class.java)
            OTTApp.getAppComponent().inject(wizardModule)
            Timber.d("RN is started")
            doInitInfiniteChain()
            isInitRn = true
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
                privateKey = primaryKey,
                storage = "level")
        compositeDisposable.add(
                wizardModule.initInfiniteChain(env)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d(it)
                                    val initResult = gson.fromJson(it, InitResult::class.java)
                                    if(TextUtils.isEmpty(appRepository.getPrivateKey())) {
                                        appRepository.saveEcKeyPair(initResult.privateKey)
                                        appRepository.saveWallet(initResult.address)
                                        addDeviceData()
                                    }
                                    wallet = appRepository.getWallet()
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    private fun addDeviceData() {
        val wallet = appRepository.getWallet()
        val addDeviceItem = AddDeviceItem(deviceId, wallet.address, "new")
        compositeDisposable.add(
                apiRepository.addDevice(addDeviceItem)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("result: $it")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun launcherStatusResp() {
        val lastAccessTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
        val updateDeviceItem = UpdateDeviceItem(
                deviceId, wallet.address, "activity", lastAccessTime)
        compositeDisposable.add(
                apiRepository.updateDevice(updateDeviceItem)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("result: $it")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun launcherAppUpdate(apps: ArrayList<AppItem>) {
        val insertManyAppItem = InsertManyAppItem(deviceId, wallet.address, apps)
        compositeDisposable.add(
                apiRepository.insertManyApps(insertManyAppItem)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("result: $it")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun insertAppExecRecord(appId: String, appName: String) {
        val startTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
        appData = AppData(appId, appName)
        ratings = Ratings(startTime)

        val insertAppExecRecordItem = InsertAppExecRecordItem(
                deviceId, wallet.address, appId, appName, startTime)
        compositeDisposable.add(
                apiRepository.insertAppExecRecord(insertAppExecRecordItem)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("@@result: $it")
                                    uuid = it.uuid
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun updateAppExecRecord(lightTxJson: String) {
        val endTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())

        ratings.appETime = endTime
        ratings.appRunduration = endTime - ratings.appSTime

        val updateAppExecRecordItem = UpdateAppExecRecordItem(
                uuid, deviceId, wallet.address, ratings.appETime, lightTxJson)
        compositeDisposable.add(
                apiRepository.updateAppExecRecord(updateAppExecRecordItem)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("result: $it")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun makeLightTx() {
        val endTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
        ratings.appETime = endTime
        ratings.appRunduration = endTime - ratings.appSTime
        appData.ratings = ratings
        val client = Client(appData, deviceId)
        val metaDataModel = MetaDataModel(client)
        val lightTx = MakeLightTx(
                fromAddress = wallet.address,
                toAddress = wallet.address,
                metadata = metaDataModel)

        compositeDisposable.add(
                wizardModule.makeLightTx(lightTx)
                        .compose(schedulerProvider.getSchedulersForSingle())
                        .subscribeBy(
                                onSuccess = {
                                    Timber.d("makeLightTx: $it")
                                    lightTxJson.value = it
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

}