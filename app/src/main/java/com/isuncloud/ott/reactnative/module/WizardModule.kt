package com.isuncloud.pingpay.reactnative.module

import android.widget.Toast
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import com.isuncloud.pingpay.database.dao.PendingDepositDao
import com.isuncloud.pingpay.database.entity.PendingDeposit
import com.isuncloud.pingpay.event.BaseRxEvent
import com.isuncloud.pingpay.event.DepositEvent
import com.isuncloud.pingpay.event.EventPublishSubject
import com.isuncloud.pingpay.model.*
import com.isuncloud.pingpay.utils.RandomGenerator
import io.reactivex.Single
import io.reactivex.SingleEmitter
import timber.log.Timber
import java.util.concurrent.TimeUnit

class WizardModule(val reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

//    var emitter: SingleEmitter<String>? = null

    override fun getName(): String {
        return "WizardModule"
    }

    private val initInfiniteChain = "initInfinitechain"
    private val makeLightTx = "makeLightTx"
    private val saveReceipt = "saveReceipt"
    private val getAssetList = "getAssetList"
    private val deposit = "deposit"
    private val withdraw = "withdraw"
    private val getProposeDeposit = "getProposeDeposit"
    private val onProposeDeposit = "onProposeDeposit"

    private lateinit var gson: Gson
    private lateinit var eventPublishSubject: EventPublishSubject<BaseRxEvent>
    private lateinit var pendingDepositDao: PendingDepositDao
    private var eventMap: HashMap<String, Pair<String, SingleEmitter<String>>> = hashMapOf()

    fun setGson(gson: Gson) {
        this.gson = gson
    }

    fun setEventPublishSubject(eventPublishSubject: EventPublishSubject<BaseRxEvent>) {
        this.eventPublishSubject = eventPublishSubject
    }

    fun setPendingDepositDao(pendingDepositDao: PendingDepositDao) {
        this.pendingDepositDao = pendingDepositDao
    }

    fun initInfiniteChain(env: EnvRequest): Single<String> {
        // Toast.makeText(reactContext, objectToString(env), Toast.LENGTH_SHORT).show()
        return sentEvent(initInfiniteChain, env)
    }

    fun makeLightTx(lightTxData: MakeLightTx): Single<String> {
        return sentEvent(makeLightTx, lightTxData)
    }

    fun saveReceipt(receipt: ReceiptModel) {
        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(saveReceipt, receipt)
    }

    fun withdraw(withdrawRequest: WithdrawRequest): Single<String> {
        return sentEvent(withdraw, withdrawRequest)
    }

    fun deposit(withdrawRequest: WithdrawRequest): Single<String> {
        return sentEvent(deposit, withdrawRequest)
    }

    private fun sentEvent(eventName: String, model: BaseModel): Single<String> {
        val eventID = RandomGenerator.uuidStringGenerator()

        val eventRequest = EventRequest()
        eventRequest.eventID = eventID
        eventRequest.request = model

        val data = objectToString(eventRequest)

        return Single.create<String> { emitter ->
            //            this.emitter = emitter
            eventMap[eventID] = Pair(eventName, emitter)
            reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, data)
        }.timeout(30, TimeUnit.SECONDS)
    }

    private fun objectToString(rawData: Any): String {
        return gson.toJson(rawData).toString()
    }

// RN call back

    @ReactMethod
    fun receiveEvent(rawData: String) {
        eventResultHandler(rawData)
    }

    @ReactMethod
    fun receiveAsyncEvent(raw: String) {
        eventResultHandler(raw)
    }

    private fun eventResultHandler(rawData: String) {
        Timber.d(rawData)

        val eventResult = gson.fromJson(rawData, EventResult::class.java)

        when {
//             TODO: DSN Listener event
            eventResult.eventID == onProposeDeposit -> {
                //FIXME: Do deposit event listener things, get password, do a remittance
                val lightTxModel = gson.fromJson(eventResult.result, LightTxModel::class.java)

                pendingDepositDao.insertPendingDeposit(PendingDeposit(lightTxModel = lightTxModel))
                eventPublishSubject.onNext(DepositEvent(lightTxModel))
                return
            }

            !eventMap.containsKey(eventResult.eventID) -> {
                return
            }

            else -> {
                val eventID = eventResult.eventID
                if (eventResult.success) {
                    eventMap[eventID]!!.second.onSuccess(objectToString(eventResult.result))
                    // Toast.makeText(reactContext, rawData, Toast.LENGTH_SHORT).show()
                } else {
//                    eventMap[eventID]!!.second.onError(RuntimeException(eventResult.result))
                }
            }
        }
    }

}