package com.isuncloud.ott.reactnative.module

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.modules.core.DeviceEventManagerModule
import io.reactivex.Single
import com.facebook.react.bridge.WritableMap

class WizardModule(val reactContext: ReactApplicationContext): ReactContextBaseJavaModule(reactContext) {

    override fun getName(): String {
        return "WizardModule"
    }

    private val initInfiniteChain = "initInfinitechain"
    private val makeLightTx = "makeLightTx"

    // first...
//    fun initInfiniteChain(env: EnvRequest): Single<String> {
//        return sentEvent(initInfiniteChain, env)
//    }

    // second...
//    fun makeLightTx(lightTxData: MakeLightTx): Single<String> {
//        return sentEvent(makeLightTx, lightTxData)
//    }


    private fun sendEvent(eventName: String, params: WritableMap) {
        reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
                .emit(eventName, params)
    }


}