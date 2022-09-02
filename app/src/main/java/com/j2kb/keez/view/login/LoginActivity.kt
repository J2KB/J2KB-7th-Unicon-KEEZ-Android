package com.j2kb.keez.view.login

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.j2kb.keez.ui.theme.KEEZTheme
import com.j2kb.keez.view.home.MainActivity
import com.j2kb.keez.view.codeblock.CodeBlockActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Google Login")
                        }
                        Button(onClick = { viewModel.login(LoginType.KAKAO, this@LoginActivity) }) {
                            Text(text = "Kakao Login")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Naver Login")
                        }
                        Button(onClick = { /*TODO*/ }) {
                            Text(text = "Apple Login")
                        }
                        Button(onClick = { viewModel.moveTo(this@LoginActivity, MainActivity::class.java) }) {
                            Text(text = "Home 화면 로그인 없이 이동")
                        }
                        Button(onClick = { viewModel.moveTo(this@LoginActivity, CodeBlockActivity::class.java) }) {
                            Text(text = "Markdown 테스트 화면 이동")
                        }
                    }
                }
            }
        }
    }
}
