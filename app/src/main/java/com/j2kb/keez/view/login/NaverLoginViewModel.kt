package com.j2kb.keez.view.login

import androidx.lifecycle.ViewModel
import com.j2kb.keez.view.home.SampleSideEffect
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class NaverLoginViewModel @Inject constructor() : ContainerHost<LoginResult, SampleSideEffect>, OAuthLogin,
    ViewModel() {

    override val container = container<LoginResult, SampleSideEffect>(LoginResult())

    fun login(activity: LoginActivity) {
        val oauthLoginCallback = object : OAuthLoginCallback {
            override fun onSuccess() {
                // 네이버 로그인 인증이 성공했을 때 수행할 코드 추가
                sendToken(NaverIdLoginSDK.getAccessToken() ?: "")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                sendException("errorCode:$errorCode, errorDesc:$errorDescription")
            }

            override fun onError(errorCode: Int, message: String) {
                onFailure(errorCode, message)
            }
        }
        NaverIdLoginSDK.authenticate(activity, oauthLoginCallback)
    }

    override fun sendToken(token: String) = intent {
        reduce { state.copy(token = token) }
    }

    override fun sendException(message: String) = intent {
        postSideEffect(SampleSideEffect.Toast(message))
    }

}