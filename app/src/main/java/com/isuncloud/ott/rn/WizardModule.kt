package com.isuncloud.ott.rn

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.google.gson.Gson
import com.isuncloud.ott.repository.model.rn.*
import com.isuncloud.ott.utils.RandomIDGenerator
import io.reactivex.Single
import io.reactivex.SingleEmitter
import timber.log.Timber
import javax.inject.Inject

class WizardModule(private val reactContext: ReactApplicationContext): ReactContextBaseJavaModule(reactContext) {

    companion object {
        private const val INIT_INFINITE_CHAIN = "initInfinitechain"
        private const val MAKE_LIGHT_TX = "makeLightTx"
    }

    @Inject lateinit var gson: Gson

    private var eventMap: HashMap<String, Pair<String, SingleEmitter<String>>> = hashMapOf()

    override fun getName(): String {
        return "WizardModule"
    }

    fun initInfiniteChain(envRequest: EnvRequest): Single<String> {
        return sendEvent(INIT_INFINITE_CHAIN, envRequest)
    }

    fun makeLightTx(lightTx: MakeLightTx): Single<String> {
        return sendEvent(MAKE_LIGHT_TX, lightTx)
    }

    @ReactMethod
    fun receiveEvent(rawData: String) {
        eventResultHandler(rawData)
    }

    @ReactMethod
    fun receiveAsyncEvent(rawData: String) {
        eventResultHandler(rawData)
    }

    private fun sendEvent(eventName: String, model: BaseModel): Single<String> {
        val eventID = RandomIDGenerator.generated()
        val eventRequest = EventRequest()
        eventRequest.eventID = eventID
        eventRequest.request = model

        val data = objectToString(eventRequest)
        Timber.d("data: " + data)

        return Single.create<String> {
            eventMap[eventID] = Pair(eventName, it)
            reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                    .emit(eventName, data)
        }
    }

    private fun eventResultHandler(rawData: String) {
        Timber.d("rawData: " + rawData)

        val eventResult = gson.fromJson(rawData, EventResult::class.java)
        val eventID = eventResult.eventID
        if (eventResult.success) {
            eventMap[eventID]!!.second.onSuccess(objectToString(eventResult.result))
        } else {

        }
    }

    private fun objectToString(rawData: Any): String {
        return gson.toJson(rawData).toString()
    }

}