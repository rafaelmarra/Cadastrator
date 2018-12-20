package com.marrapps.cadastrator.interfaces

import com.marrapps.cadastrator.model.Cep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ServiceCep {

    @GET("{CEP}/json/")
    fun getCep(@Path("CEP") CEP : String) : Call<Cep>
}