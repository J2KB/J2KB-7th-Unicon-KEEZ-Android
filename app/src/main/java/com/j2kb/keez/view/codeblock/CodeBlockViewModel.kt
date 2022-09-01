package com.j2kb.keez.view.codeblock

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.j2kb.keez.view.home.SampleSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import io.noties.markwon.syntax.Prism4jSyntaxHighlight
import io.noties.markwon.syntax.Prism4jThemeDefault
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
    include = ["java", "kotlin", "swift"],
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
            val highlight = Prism4jSyntaxHighlight.create(prism, Prism4jThemeDefault.create(0))
            val language = when (code.language) {
                CodeBlock.Language.KOTLIN -> "kotlin"
                CodeBlock.Language.JAVA -> "java"
                CodeBlock.Language.SWIFT -> "swift"
            }

            reduce { highlight.highlight(language, code.sourceCode) }
        }

    }

    private fun getCodeString(): String {
        return """import UIKit

class ViewController: UIViewController {
    let timeSelector: Selector = #selector(ViewController.updateTime)
    let interval = 1.0
    var count = 0
    
    @IBOutlet weak var lblCurrentTime: UILabel!
    @IBOutlet weak var lblPickerTime: UILabel!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        Timer.scheduledTimer(timeInterval: interval, target: self, selector: timeSelector, userInfo: nil, repeats: true)
    }

    @IBAction func changeDatePicker(_ sender: UIDatePicker) {
        let datePickerView = sender
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm EEE"
        lblPickerTime.text = "선택시간: " + formatter.string(from: datePickerView.date)
    }
    
    @objc func updateTime() {
        let date = NSDate()
        
        let formatter = DateFormatter()
        formatter.dateFormat = "yyyy-MM-dd HH:mm:ss EEE"
        lblCurrentTime.text = "현재시간: " + formatter.string(from: date as Date)
    }
    
}
    """
    }
}