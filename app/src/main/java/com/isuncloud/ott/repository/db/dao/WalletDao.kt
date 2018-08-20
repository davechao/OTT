package com.isuncloud.ott.repository.db.dao

import android.arch.persistence.room.*
import com.isuncloud.ott.repository.model.Wallet

@Dao
interface WalletDao {

    @Insert
    fun insertWallet(wallet: Wallet)

    @Update
    fun updateWallet(wallet: Wallet)

    @Delete
    fun deleteWallet(wallet: Wallet)

    @Query("DELETE FROM wallet")
    fun deleteAllWallets()

    @Query("SELECT * FROM wallet")
    fun loadWallets(): List<Wallet>

}