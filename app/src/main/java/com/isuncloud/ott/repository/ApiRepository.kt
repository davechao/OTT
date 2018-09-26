package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.model.api.*
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiRepository @Inject constructor(private val apiService: ApiService) {

    companion object {
        const val MUTATION = "mutation"
        const val ADD_DEVICE = "addDevice"
        const val UPDATE_DEVICE = "updateDevice"
        const val INSERT_MANY_APPS = "insertManyApps"
        const val INSERT_APP_EXEC_RECORD = "insertAppExecRecord"
        const val UPDATE_APP_EXEC_RECORD = "updateAppExecRecord"
    }

    private val returnField = "{ok}"
    private val appExecReturnFieldItem = "{ok uuid}"

    fun addDevice(body: AddDeviceItem)
            : Completable = apiService.addDevice(MUTATION, ADD_DEVICE, body, returnField)
            .ignoreElement()

    fun updateDevice(body: UpdateDeviceItem)
            : Completable = apiService.updateDevice(MUTATION, UPDATE_DEVICE, body, returnField)
            .ignoreElement()

    fun insertManyApps(body: InsertManyAppItem)
            : Completable = apiService.insertManyApp(MUTATION, INSERT_MANY_APPS, body, returnField)
            .ignoreElement()

    fun insertAppExecRecord(body: InsertAppExecRecordItem)
            : Single<ApiItem<AppExecReturnFieldItem>> = apiService.insertAppExecRecord(MUTATION, INSERT_APP_EXEC_RECORD, body, appExecReturnFieldItem)

    fun updateAppExecRecord(body: UpdateAppExecRecordItem)
            : Completable = apiService.updateAppExecRecord(MUTATION, UPDATE_APP_EXEC_RECORD, body, returnField)
            .ignoreElement()

}