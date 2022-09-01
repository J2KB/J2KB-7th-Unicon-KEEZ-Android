package com.j2kb.keez.view.codeblock

import android.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.syntax.Prism4jSyntaxHighlight
import io.noties.markwon.syntax.Prism4jThemeDarkula
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
@PrismBundle(
    include = ["swift"],
    grammarLocatorClassName = ".GrammarLocatorSourceCode"
)
class CodeBlockViewModel @Inject constructor() : ContainerHost<CharSequence, SampleSideEffect>,
    ViewModel() {

    override val container = container<CharSequence, SampleSideEffect>(" ".subSequence(0, 0))
    private var job: Job? = null

    init {
        getCodeBlock()
    }

    private fun getCodeBlock() = intent {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            /**
             * TODO
             *  추후에 서버에서 받아오는 방식으로 변경
             *  getCodeString() */
            val codeString = getCodeString()
            val code: CodeBlock.Code = CodeBlock.Code(CodeBlock.Language.SWIFT, codeString)

            val prism = Prism4j(GrammarLocatorSourceCode())
            val highlight = Prism4jSyntaxHighlight.create(prism, Prism4jThemeDarkula.create(Color.WHITE))
            val language = when (code.language) {
                CodeBlock.Language.SWIFT -> "swift"
            }

            reduce { highlight.highlight(language, code.sourceCode) }
        }

    }

    private fun getCodeString(): String {
        return """
            let greeting = "Hello J2KB!"
            print(greeting)
    """
    }
}