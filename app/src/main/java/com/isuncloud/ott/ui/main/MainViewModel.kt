package com.isuncloud.ott.ui.main

import android.app.Application
import androidx.lifecycle.MutableLiveData
import android.text.TextUtils
import com.facebook.react.ReactInstanceManager
import com.hybroad.hypacketlib.HyPacket
import com.isuncloud.ott.BuildConfig
import com.isuncloud.ott.ui.base.BaseAndroidViewModel
import io.reactivex.disposables.CompositeDisposable
import com.isuncloud.ott.OTTApp
import com.isuncloud.ott.repository.ApiRepository
import com.isuncloud.ott.repository.AppRepository
import com.isuncloud.ott.repository.db.typeconverter.LocalDateTimeConverter
import com.isuncloud.ott.repository.model.AppData
import com.isuncloud.ott.repository.model.Packet
import com.isuncloud.ott.repository.model.Ratings
import com.isuncloud.ott.repository.model.Wallet
import com.isuncloud.ott.repository.model.api.*
import com.isuncloud.ott.repository.model.rn.*
import com.isuncloud.ott.rn.WizardModule
import com.isuncloud.ott.utils.SchedulerProvider
import io.reactivex.rxkotlin.subscribeBy
import org.spongycastle.util.encoders.Hex
import org.threeten.bp.LocalDateTime
import timber.log.Timber
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

class MainViewModel(app: Application): BaseAndroidViewModel(app) {

    companion object {
        private const val PACKET_CAPTURE_PERIOD = 5000
    }

    private val compositeDisposable by lazy { CompositeDisposable() }

    private lateinit var wizardModule: WizardModule
    private lateinit var wallet: Wallet
    private lateinit var appData: AppData
    private lateinit var ratings: Ratings
    private lateinit var packetCaptureTimer: Timer
    private lateinit var packets: ArrayList<Packet>

    private var uuid = ""
    var deviceId = ""
    var isClickApp = false
    var isInitRn = false
    var lightTxJson = MutableLiveData<String>()

    @Inject lateinit var reactInstanceManager: ReactInstanceManager
    @Inject lateinit var schedulerProvider: SchedulerProvider
    @Inject lateinit var appRepository: AppRepository
    @Inject lateinit var apiRepository: ApiRepository
    @Inject lateinit var hyPacket: HyPacket

    override fun onCleared() {
        compositeDisposable.clear()
        compositeDisposable.dispose()
    }

    init {
        OTTApp.getAppComponent().inject(this)
    }

    fun startHyPacketCapture(appId: String, appName: String) {
        Timber.d("HyPacket startCapture")
        packets = arrayListOf()
        hyPacket.startCapture()

        val packetCaptureTimerTask = object: TimerTask() {
            override fun run() {
                val packetData = hyPacket.packet
                val data = Hex.toHexString(packetData)
                Timber.d("Packet Data:  $data")
                val packet = Packet(
                        data = packetData,
                        appId = appId,
                        appName = appName,
                        deviceId = deviceId,
                        createTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
                )
                packets.add(packet)
            }
        }
        packetCaptureTimer = Timer()
        packetCaptureTimer.schedule(packetCaptureTimerTask, 0, PACKET_CAPTURE_PERIOD.toLong())
    }

    fun stopHyPacketCapture() {
        Timber.d("HyPacket stopCapture")
        hyPacket.stopCapture()
        packetCaptureTimer.cancel()
        if(packets.isNotEmpty()) {
            appRepository.savePackets(packets)
            packets.clear()
        }
    }

    fun setupReactInstanceManagerListener() {
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
                                    if(TextUtils.isEmpty(appRepository.getPrivateKey())) {
                                        appRepository.saveEcKeyPair(it.privateKey)
                                        appRepository.saveWallet(it.address)
                                        addDeviceData()
                                    } else {
                                        wallet = appRepository.getWallet()
                                    }
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    private fun addDeviceData() {
        wallet = appRepository.getWallet()
        val addDeviceItem = AddDeviceItem(deviceId, wallet.address, "new")
        compositeDisposable.add(
                apiRepository.addDevice(addDeviceItem)
                        .compose(schedulerProvider.getSchedulersForCompletable())
                        .subscribeBy(
                                onComplete = {
                                    Timber.d("Device data is added!")
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
                        .compose(schedulerProvider.getSchedulersForCompletable())
                        .subscribeBy(
                                onComplete = {
                                    Timber.d("Device data is updated!")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun launcherAppUpdate(apps: ArrayList<AppItem>) {
        val insertManyAppItem = InsertManyAppItem(deviceId, wallet.address, apps)
        compositeDisposable.add(
                apiRepository.insertManyApps(insertManyAppItem)
                        .compose(schedulerProvider.getSchedulersForCompletable())
                        .subscribeBy(
                                onComplete = {
                                    Timber.d("Apps are inserted!")
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
                                    Timber.d("result: $it")
                                    uuid = it.data.uuid
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

    fun makeTxAndUpdateAppExecRecord() {
        val endTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
        ratings.appETime = endTime
        ratings.appRunduration = endTime - ratings.appSTime
        appData.ratings = ratings

        val client = Client(appData, deviceId)
        val server = Server()

        val metaDataModel = MetaDataModel(client, server)

        val lightTx = MakeLightTx(
                fromAddress = wallet.address,
                toAddress = wallet.address,
                metadata = metaDataModel)

        compositeDisposable.add(
                wizardModule.makeLightTx(lightTx)
                        .compose(schedulerProvider.getSchedulersForObservable())
                        .doOnNext { updateAppExecRecord(it) }
                        .subscribeBy(
                                onComplete = {
                                    Timber.d("MakeTx and update app execute record!")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }
                        ))
    }

    private fun updateAppExecRecord(lightTxJson: String) {
        if(TextUtils.isEmpty(uuid)) {
            return
        }

        val endTime = LocalDateTimeConverter().dateToTimestamp(LocalDateTime.now())
        ratings.appETime = endTime
        ratings.appRunduration = endTime - ratings.appSTime

        val updateAppExecRecordItem = UpdateAppExecRecordItem(
                uuid, deviceId, wallet.address, ratings.appETime, lightTxJson)
        compositeDisposable.add(
                apiRepository.updateAppExecRecord(updateAppExecRecordItem)
                        .compose(schedulerProvider.getSchedulersForCompletable())
                        .subscribeBy(
                                onComplete = {
                                    Timber.d("App execute record is updated!")
                                },
                                onError = {
                                    Timber.d(it.toString())
                                }))
    }

}