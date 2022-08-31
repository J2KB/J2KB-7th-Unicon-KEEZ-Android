package com.j2kb.keez.view.markdown_test

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TestViewModel @Inject constructor() : ViewModel() {
    fun getTestMarkdown(activity: MarkdownTestActivity) {
        val sample = CodeBlockSample()

    }
}