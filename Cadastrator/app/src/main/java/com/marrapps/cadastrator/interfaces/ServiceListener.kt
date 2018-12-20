package com.marrapps.cadastrator.interfaces

interface ServiceListener {

    fun onSucess(obtained: Any)

    fun onError(throwable: Throwable)
}