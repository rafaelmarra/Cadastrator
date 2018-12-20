package com.marrapps.cadastrator.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Cep(

    @SerializedName("cep")
    @Expose
    val cep: String,

    @SerializedName("logradouro")
    @Expose
    val address: String,

    @SerializedName("bairro")
    @Expose
    val neighbourhood: String,

    @SerializedName("localidade")
    @Expose
    val city: String,

    @SerializedName("uf")
    @Expose
    val state: String,

    @SerializedName("erro")
    @Expose
    val error: Boolean
)