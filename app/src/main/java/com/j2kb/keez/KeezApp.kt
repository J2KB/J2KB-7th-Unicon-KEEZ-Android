package com.j2kb.keez

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@HiltAndroidApp
class KeezApp: Application() {

    override fun onCreate() {
        super.onCreate()
        executorService = Executors.newCachedThreadPool()
    }

    companion object {
        lateinit var executorService: ExecutorService
    }
}