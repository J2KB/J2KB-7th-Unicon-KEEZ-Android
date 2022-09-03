package com.j2kb.keez.view.login

import android.app.Activity
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.j2kb.keez.ui.theme.KEEZTheme
import com.j2kb.keez.view.codeblock.CodeBlockActivity
import com.j2kb.keez.view.home.MainActivity
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val kakaoLoginViewModel: KakaoLoginViewModel by viewModels()
    private val googleLoginViewModel: GoogleLoginViewModel by viewModels()

    private val googleLoginLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                googleLoginViewModel.handleSignInResult(result.data)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        googleLoginViewModel.settingForGoogleLogin(this@LoginActivity)

        setContent {
            KEEZTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = { googleLoginViewModel.login(googleLoginLauncher) }) {
                            Text(text = "Google Login")
                        }
                        Button(onClick = { kakaoLoginViewModel.login(this@LoginActivity) }) {
                            Text(text = "Kakao Login")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Naver Login")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Apple Login")
                        }
                        Button(onClick = {
                            moveTo(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        }) {
                            Text(text = "Home 화면 로그인 없이 이동")
                        }
                        Button(onClick = {
                            moveTo(
                                this@LoginActivity,
                                CodeBlockActivity::class.java
                            )
                        }) {
                            Text(text = "Markdown 테스트 화면 이동")
                        }
                        setHiddenEffect()
                    }
                }
            }
        }
    }

    @Composable
    private fun setHiddenEffect() {
        LaunchKakaoEffect()
        LaunchGoogleEffect()
        WaitToMove()
    }

    @Composable
    private fun LaunchKakaoEffect() {
        LaunchedEffect(kakaoLoginViewModel) {
            kakaoLoginViewModel.container.sideEffectFlow.collect {
                showSideEffectToast(it)
            }
        }

    }

    @Composable
    private fun LaunchGoogleEffect() {
        LaunchedEffect(googleLoginViewModel) {
            googleLoginViewModel.container.sideEffectFlow.collect {
                showSideEffectToast(it)
            }
        }
    }

    private fun showSideEffectToast(it: SampleSideEffect) {
        Toast.makeText(
            applicationContext,
            (it as SampleSideEffect.Toast).test,
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun moveTo(activity: Activity, targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity).addFlags(FLAG_ACTIVITY_NEW_TASK))
    }

    @Composable
    private fun WaitToMove() {
        val kakaoState by kakaoLoginViewModel.container.stateFlow.collectAsState()
        val googleState by googleLoginViewModel.container.stateFlow.collectAsState()

        findToken(kakaoState, googleState).let { token ->
            if (token.isNotEmpty()) {
                startActivity(
                    Intent(
                        this@LoginActivity,
                        MainActivity::class.java
                    ).addFlags(FLAG_ACTIVITY_NEW_TASK)
                        .putExtra("token", token)
                )
                finish()
            }
        }
    }

    @Composable
    private fun findToken(
        kakaoState: LoginResult,
        googleState: LoginResult
    ): String {
        if (kakaoState.token.isNotEmpty()) {
            return kakaoState.token
        } else if (googleState.token.isNotEmpty()) {
            return googleState.token
        }
        return ""
    }
}
