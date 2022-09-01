package com.j2kb.keez.view.codeblock

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.j2kb.keez.ui.theme.KEEZTheme
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CodeBlockActivity : ComponentActivity() {

    private val viewModel: CodeBlockViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KEEZTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White
                ) {
                    CodeBlockView(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun CodeBlockView(modifier: Modifier = Modifier) {
        val codeBlock by viewModel.container.stateFlow.collectAsState()

        LaunchToastSideEffect()

        AndroidView(
            modifier = modifier, factory = { context -> TextView(context) },
            update = {
                it.text = codeBlock
            },
        )
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