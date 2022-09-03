package com.j2kb.keez.view.login

import android.app.Activity
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.j2kb.keez.KeezApp
import com.j2kb.keez.view.home.SampleSideEffect
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject


@HiltViewModel
class KakaoLoginViewModel @Inject constructor() : ContainerHost<LoginResult, SampleSideEffect>, ViewModel() {

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    override val container = container<LoginResult, SampleSideEffect>(LoginResult())

    // FIXME Refactoring
    fun login(activity: Activity) {
        // 카카오계정으로 로그인 공통 callback 구성
        // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(KeezApp.TAG, "카카오계정으로 로그인 실패", error)
                sendException("카카오계정으로 로그인 실패")
            } else if (token != null) {
                Log.i(KeezApp.TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
                sendToken(token.accessToken)
            }
        }

        // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
        if (UserApiClient.instance.isKakaoTalkLoginAvailable(activity)) {
            UserApiClient.instance.loginWithKakaoTalk(activity) { token, error ->
                if (error != null) {
                    Log.e(KeezApp.TAG, "카카오톡으로 로그인 실패", error)
                    sendException("카카오톡 앱으로 로그인 실패")

                    // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                    // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        sendException("카카오톡 로그인 취소")
                        return@loginWithKakaoTalk
                    }

                    // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                    UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
                } else if (token != null) {
                    Log.i(KeezApp.TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    sendToken(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(activity, callback = callback)
        }
    }

    private fun sendToken(token: String) = intent {
        reduce { state.copy(token = token) }
    }

    private fun sendException(message: String) = intent {
        postSideEffect(SampleSideEffect.Toast(message))
    }
}