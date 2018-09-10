package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.model.api.*
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
            : Single<ApiItem<ReturnFieldItem>> = apiService.addDevice(MUTATION, ADD_DEVICE, body, returnField)

    fun updateDevice(body: UpdateDeviceItem)
            : Single<ApiItem<ReturnFieldItem>> = apiService.updateDevice(MUTATION, UPDATE_DEVICE, body, returnField)

    fun insertManyApps(body: InsertManyAppItem)
            : Single<ApiItem<ReturnFieldItem>> = apiService.insertManyApp(MUTATION, INSERT_MANY_APPS, body, returnField)

    fun insertAppExecRecord(body: InsertAppExecRecordItem)
            : Single<ApiItem<AppExecReturnFieldItem>> = apiService.insertAppExecRecord(MUTATION, INSERT_APP_EXEC_RECORD, body, appExecReturnFieldItem)

    fun updateAppExecRecord(body: UpdateAppExecRecordItem)
            : Single<ApiItem<ReturnFieldItem>> = apiService.updateAppExecRecord(MUTATION, UPDATE_APP_EXEC_RECORD, body, returnField)

}