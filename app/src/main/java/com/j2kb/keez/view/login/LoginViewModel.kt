package com.j2kb.keez.view.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.util.Log
import androidx.lifecycle.ViewModel
import com.j2kb.keez.KeezApp
import com.j2kb.keez.view.home.MainActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {
    fun moveTo(activity: Activity, targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity).addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    fun login(loginType: LoginType, activity: Activity) {
        when (loginType) {
            LoginType.KAKAO -> loginByKakao(activity)
            else -> return
        }
    }

    private fun loginByKakao(activity: Activity) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(KeezApp.TAG, "카카오계정으로 로그인 실패", error)
            } else if (token != null) {
                Log.i(KeezApp.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                moveToHome(activity)
            }
        }

        val context = activity.applicationContext
        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
            UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                if (error != null) {
                    Log.e(KeezApp.TAG, "카카오톡으로 로그인 실패", error)

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                } else if (token != null) {
                    Log.i(KeezApp.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    moveToHome(activity)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
        }
    }

    private fun moveToHome(activity: Activity) {
        moveTo(activity, MainActivity::class.java)
        activity.finish()
    }
}