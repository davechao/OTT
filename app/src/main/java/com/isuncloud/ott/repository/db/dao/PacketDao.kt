package com.isuncloud.ott.repository.db.dao

import android.arch.persistence.room.*
import com.isuncloud.ott.repository.model.Packet

@Dao
interface PacketDao {

    @Insert
    fun insertPacket(packet: Packet)

    @Insert
    fun insertPackets(packet: List<Packet>)

    @Update
    fun updatePacket(packet: Packet)

    @Delete
    fun deletePacket(packet: Packet)

    @Query("DELETE FROM packet")
    fun deleteAllPackets()

    @Query("SELECT * FROM packet")
    fun loadPackets(): List<Packet>
}