package com.j2kb.keez.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.j2kb.keez.BuildConfig
import com.j2kb.keez.KeezApp
import com.j2kb.keez.view.login.LoginActivity

object Google {

    fun init(
        activity: LoginActivity,
        sendToken: (String) -> Unit,
        sendException: (String) -> Unit
    ): Pair<GoogleSignInClient, ActivityResultLauncher<Intent>> {

        val client = GoogleSignIn.getClient(
            activity, GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode(BuildConfig.GOOGLE_SERVER_KEY)
                .requestEmail()
                .build()
        )

        val resultLauncher =
            activity.registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    try {
                        val account: GoogleSignInAccount =
                            GoogleSignIn.getSignedInAccountFromIntent(result.data)
                                .getResult(ApiException::class.java)
                        Log.i(KeezApp.TAG, account.serverAuthCode ?: "없음")
                        sendToken(account.serverAuthCode ?: "")
                    } catch (e: ApiException) {
                        Log.w(KeezApp.TAG, "signInResult:failed code=" + e.statusCode)
                        sendException("Google 로그인 실패")
                    }
                }
            }
        return Pair(client, resultLauncher)
    }

    fun login(
        mGoogleSignInClient: GoogleSignInClient,
        googleLoginLauncher: ActivityResultLauncher<Intent>
    ) {
        val signInIntent = mGoogleSignInClient.signInIntent
        googleLoginLauncher.launch(signInIntent)
    }

}