package com.marrapps.cadastrator.database

import android.os.Handler
import android.os.HandlerThread

class DbWorkerThread(threadName: String) : HandlerThread(threadName) {

    private var mWorkerHandler: Handler = Handler()

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        mWorkerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        mWorkerHandler.post(task)
    }

}