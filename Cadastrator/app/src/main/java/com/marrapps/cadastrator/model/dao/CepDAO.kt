package com.marrapps.cadastrator.model.dao

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
import com.marrapps.cadastrator.interfaces.ServiceListener
import com.marrapps.cadastrator.model.Cep
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CepDAO {

    fun getCep(context: Context, cep: String, listener: ServiceListener) {

        if (isConnected(context)) {

            val call: Call<Cep> = RetrofitService().serviceCep().getCep(cep)
            call.enqueue(object : Callback<Cep> {

                override fun onResponse(call: Call<Cep>, response: Response<Cep>) {
                    if (response.body() != null) {
                        listener.onSucess(response.body() as Cep)
                    }
                }

                override fun onFailure(call: Call<Cep>, t: Throwable) {
                    listener.onError(t)
                }
            })

        } else {
            Toast.makeText(context, "Não foi possível conectar à internet", Toast.LENGTH_LONG).show()
        }
    }

    private fun isConnected(context: Context): Boolean {

        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo

        return networkInfo?.isConnected == true
    }
}