package com.marrapps.cadastrator.interfaces

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.marrapps.cadastrator.model.Entry

@Dao
interface RoomDAOEntry {

    @Query("SELECT * from entryData")
    fun getAll(): List<Entry>

    @Query("SELECT * from entryData")
    fun getAllLiveData(): LiveData<List<Entry>>

    @Update
    fun update(entry: Entry)

    @Insert(onConflict = REPLACE)
    fun insert(entry: Entry)

    @Delete
    fun delete(entry: Entry)
}