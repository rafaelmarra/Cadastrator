package com.marrapps.cadastrator.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.marrapps.cadastrator.interfaces.RoomDAOEntry
import com.marrapps.cadastrator.model.Entry

@Database(entities = arrayOf(Entry::class), version = 1, exportSchema = false)
abstract class EntryDatabase : RoomDatabase() {

    abstract fun entryDao(): RoomDAOEntry

    companion object {
        private var INSTANCE: EntryDatabase? = null

        fun getInstance(context: Context): EntryDatabase? {
            if (INSTANCE == null) {
                synchronized(EntryDatabase::class) {
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext,
                        EntryDatabase::class.java, "entry.db"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}