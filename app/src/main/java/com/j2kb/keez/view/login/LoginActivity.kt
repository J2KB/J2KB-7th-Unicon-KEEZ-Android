package com.j2kb.keez.view.login

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import com.j2kb.keez.auth.LoginType
import com.j2kb.keez.ui.theme.KEEZTheme
import com.j2kb.keez.view.home.MainActivity
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.initGoogleLogin(this@LoginActivity)

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
                        Button(onClick = {
                            viewModel.login(
                                LoginType.GOOGLE,
                                this@LoginActivity
                            )
                        }) {
                            Text(text = "Google Login")
                        }
                        Button(onClick = {
                            viewModel.login(
                                LoginType.KAKAO,
                                this@LoginActivity
                            )
                        }) {
                            Text(text = "Kakao Login")
                        }
                        Button(onClick = {
                            viewModel.login(
                                LoginType.NAVER,
                                this@LoginActivity
                            )
                        }) {
                            Text(text = "Naver Login")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Apple Login")
                        }
                        setHiddenEffect()
                    }
                }
            }
        }
    }

    @Composable
    private fun setHiddenEffect() {
        setSideEffect()
        setToMoveHome()
    }

    @Composable
    private fun setToMoveHome() {
        val state by viewModel.container.stateFlow.collectAsState()

        state.token.let { token ->
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
    private fun setSideEffect() {
        LaunchedEffect(viewModel) {
            viewModel.container.sideEffectFlow.collect {
                Toast.makeText(
                    applicationContext,
                    (it as SampleSideEffect.Toast).test,
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
