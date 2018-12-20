package com.marrapps.cadastrator.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "entryData")
data class Entry(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") var id: Long?,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "cpf") var cpf: String,
    @ColumnInfo(name = "birth") var birth: String,
    @ColumnInfo(name = "cep") var cep: String,
    @ColumnInfo(name = "address") var address: String,
    @ColumnInfo(name = "neighbourhood") var neighbourhood: String,
    @ColumnInfo(name = "city") var city: String,
    @ColumnInfo(name = "state") var state: String,
    @ColumnInfo(name = "number") var number: String,
    @ColumnInfo(name = "compl") var compl: String
): Serializable {

    constructor(): this(null, "", "", "", "", "", "", "", "", "", "")

}