package com.j2kb.keez.auth

import com.j2kb.keez.view.login.LoginActivity
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.OAuthLoginCallback

object Naver {
    fun login(
        activity: LoginActivity,
        onSuccessCallback: (String) -> Unit,
        onFailureCallback: (String) -> Unit,
        onErrorCallback: (String) -> Unit,
    ) {
        NaverIdLoginSDK.authenticate(activity, object : OAuthLoginCallback {
            override fun onError(errorCode: Int, message: String) {
                onErrorCallback("errorCode:$errorCode, errorDesc:$message")
            }

            override fun onFailure(httpStatus: Int, message: String) {
                val errorCode = NaverIdLoginSDK.getLastErrorCode().code
                val errorDescription = NaverIdLoginSDK.getLastErrorDescription()
                onFailureCallback("errorCode:$errorCode, errorDesc:$errorDescription")
            }

            override fun onSuccess() {
                onSuccessCallback(NaverIdLoginSDK.getAccessToken() ?: "")
            }

        })
    }
}
