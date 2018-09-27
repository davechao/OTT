package com.isuncloud.ott.repository

import com.isuncloud.ott.repository.model.api.*
import io.reactivex.Completable
import io.reactivex.Flowable
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @POST("/graphql_gateway/{type}/{action}")
    fun addDevice(
            @Path("type") type: String,
            @Path("action") action: String,
            @Body body: AddDeviceItem,
            @Query("r_fields") rFields: String
    ): Completable

    @POST("/graphql_gateway/{type}/{action}")
    fun updateDevice(
            @Path("type") type: String,
            @Path("action") action: String,
            @Body body: UpdateDeviceItem,
            @Query("r_fields") rFields: String
    ): Completable

    @POST("/graphql_gateway/{type}/{action}")
    fun insertManyApp(
            @Path("type") type: String,
            @Path("action") action: String,
            @Body body: InsertManyAppItem,
            @Query("r_fields") rFields: String
    ): Completable

    @POST("/graphql_gateway/{type}/{action}")
    fun insertAppExecRecord(
            @Path("type") type: String,
            @Path("action") action: String,
            @Body body: InsertAppExecRecordItem,
            @Query("r_fields") rFields: String
    ): Flowable<ApiItem<AppExecReturnFieldItem>>

    @POST("/graphql_gateway/{type}/{action}")
    fun updateAppExecRecord(
            @Path("type") type: String,
            @Path("action") action: String,
            @Body body: UpdateAppExecRecordItem,
            @Query("r_fields") rFields: String
    ): Completable

}