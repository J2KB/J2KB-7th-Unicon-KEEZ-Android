package com.j2kb.keez

import android.app.Application
import com.kakao.sdk.common.KakaoSdk
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@HiltAndroidApp
class KeezApp: Application() {

    override fun onCreate() {
        super.onCreate()
        executorService = Executors.newCachedThreadPool()
        KakaoSdk.init(this, BuildConfig.KAKAO_APPKEY)
    }

    companion object {
        lateinit var executorService: ExecutorService
        const val TAG = "KEEZ"
    }
}