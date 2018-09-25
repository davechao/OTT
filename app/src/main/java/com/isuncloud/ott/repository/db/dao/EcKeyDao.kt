package com.isuncloud.ott.repository.db.dao

import androidx.room.*
import com.isuncloud.ott.repository.model.EcKey

@Dao
interface EcKeyDao {

    @Insert
    fun insertEcKey(ecKey: EcKey)

    @Update
    fun updateEcKey(ecKey: EcKey)

    @Delete
    fun deleteEcKey(ecKey: EcKey)

    @Query("DELETE FROM eckey")
    fun deleteAllEcKeys()

    @Query("SELECT * FROM eckey")
    fun loadEcKeys(): List<EcKey>

}