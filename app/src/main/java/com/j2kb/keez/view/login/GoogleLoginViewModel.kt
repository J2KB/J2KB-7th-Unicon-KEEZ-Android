package com.j2kb.keez.view.login

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.ViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.j2kb.keez.BuildConfig
import com.j2kb.keez.KeezApp
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class GoogleLoginViewModel @Inject constructor() : ContainerHost<LoginResult, SampleSideEffect>,
    OAuthLogin,
    ViewModel() {
    override val container = container<LoginResult, SampleSideEffect>(LoginResult())

    private lateinit var mGoogleSignInClient: GoogleSignInClient

    fun settingForGoogleLogin(activity: Activity) {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestServerAuthCode(BuildConfig.GOOGLE_SERVER_KEY)
            .requestEmail()
            .build()
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(activity, gso)
    }

    fun login(loginLauncher: ActivityResultLauncher<Intent>?) {
        val signInIntent = mGoogleSignInClient.signInIntent
        loginLauncher?.launch(signInIntent)
    }

    fun handleSignInResult(data: Intent?) = intent {
        val completedTask: Task<GoogleSignInAccount> =
            GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            val account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)
            Log.i(KeezApp.TAG, account.serverAuthCode ?: "없음")
            sendToken(account.serverAuthCode ?: "")
        } catch (e: ApiException) {
            Log.w(KeezApp.TAG, "signInResult:failed code=" + e.statusCode)
            sendException("Google 로그인 실패")
        }
    }

    override fun sendToken(token: String) = intent {
        reduce { state.copy(token = token) }
    }

    override fun sendException(message: String) = intent {
        postSideEffect(SampleSideEffect.Toast(message))
    }
}