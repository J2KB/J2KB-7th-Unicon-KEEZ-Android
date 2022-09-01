package com.j2kb.keez.view.codeblock

import android.os.Bundle
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.j2kb.keez.ui.theme.KEEZTheme
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
                    color = MaterialTheme.colors.background
                ) {
                    MarkdownView(
                        viewModel.getTestCodeBlock(),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun MarkdownView(text: CharSequence, modifier: Modifier = Modifier) {
        AndroidView(
            modifier = modifier, factory = { context -> TextView(context) },
            update = {
                it.text = text
            },
        )
    }
}