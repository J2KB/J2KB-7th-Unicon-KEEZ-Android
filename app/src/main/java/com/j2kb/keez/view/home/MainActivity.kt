package com.j2kb.keez.view.home

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
import com.j2kb.keez.ui.theme.KEEZTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: SampleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, intent.getStringExtra("token"), Toast.LENGTH_SHORT).show()

        setContent {
            KEEZTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            viewModel.getData()
                        }) {
                            Text("샘플 데이터 불러오기")
                        }
                        ResultText()
                    }
                }
            }
        }
    }

    @Composable
    private fun ResultText() {
        val state by viewModel.container.stateFlow.collectAsState()

        LaunchToastSideEffect()

        Text("id: ${state.id}, name: ${state.name}, values: ${state.values}")
    }

    @Composable
    private fun LaunchToastSideEffect() {
        LaunchedEffect(viewModel) {
            viewModel.container.sideEffectFlow.collect {
                when (it) {
                    is SampleSideEffect.Toast -> Toast.makeText(
                        applicationContext,
                        it.test,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}
