package com.j2kb.keez.view.login

import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.j2kb.keez.auth.*
import com.j2kb.keez.view.home.SampleSideEffect
import com.navercorp.nid.NaverIdLoginSDK
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ContainerHost<LoginResult, SampleSideEffect>,
    ViewModel() {

    override val container = container<LoginResult, SampleSideEffect>(LoginResult())

    private lateinit var googleClient: GoogleSignInClient
    private lateinit var googleLauncher: ActivityResultLauncher<Intent>

    fun initGoogleLogin(activity: LoginActivity) {
        Google.init(activity, { sendToken(it) }, { sendException(it) }).let {
            googleClient = it.first
            googleLauncher = it.second
        }
    }

    fun login(loginType: LoginType, activity: LoginActivity) {

        when (loginType) {
            LoginType.GOOGLE -> Google.login(googleClient, googleLauncher)
            LoginType.KAKAO -> Kakao.login(activity) { token, error ->
                if (error != null) {
                    sendException("카카오계정으로 로그인 실패")
                } else if (token != null) {
                    sendToken(token.accessToken)
                }
            }
            LoginType.NAVER -> Naver.login(
                activity,
                onSuccessCallback = { sendToken(NaverIdLoginSDK.getAccessToken() ?: "") },
                onFailureCallback = { sendException(it) },
                onErrorCallback = { sendException(it) })
            else -> return
        }
    }

    private fun sendToken(token: String) = intent {
        reduce { state.copy(token = token) }
    }

    private fun sendException(message: String) = intent {
        postSideEffect(SampleSideEffect.Toast(message))
    }
}