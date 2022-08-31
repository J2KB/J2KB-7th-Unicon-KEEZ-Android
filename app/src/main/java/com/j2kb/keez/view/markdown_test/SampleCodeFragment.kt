package com.j2kb.keez.view.markdown_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.j2kb.keez.KeezApp
import com.j2kb.keez.R
import io.noties.markwon.syntax.Prism4jSyntaxHighlight
import io.noties.markwon.syntax.Prism4jThemeDefault
import io.noties.prism4j.Prism4j
import io.noties.prism4j.annotations.PrismBundle


@PrismBundle(include = ["java", "kotlin", "swift"], grammarLocatorClassName = ".GrammarLocatorSourceCode")
class SampleCodeFragment : Fragment() {

    private lateinit var progressBar: View
    private lateinit var textView: TextView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sample_code, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.progress_bar)
        textView = view.findViewById(R.id.text_view)

        load()
    }

    private fun load() {
        KeezApp.executorService.submit {
            val code: Sample.Code = Sample.Code(Sample.Language.SWIFT, """import UIKit

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
    """)
            val prism = Prism4j(GrammarLocatorSourceCode())
            val highlight = Prism4jSyntaxHighlight.create(prism, Prism4jThemeDefault.create(0))
            val language = when (code.language) {
                Sample.Language.KOTLIN -> "kotlin"
                Sample.Language.JAVA -> "java"
                Sample.Language.SWIFT -> "swift"
            }
            val text = highlight.highlight(language, code.sourceCode)

            textView.post {
                //attached
                if (context != null) {
                    progressBar.hidden = true
                    textView.text = text
                }
            }
        }
    }

    private val sample: Sample by lazy(LazyThreadSafetyMode.NONE) {
        val temp: Sample = (arguments!!.getParcelable(ARG_SAMPLE))!!
        temp
    }

    companion object {
        private const val ARG_SAMPLE = "arg.Sample"

        fun init(sample: Sample): SampleCodeFragment {
            return SampleCodeFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(ARG_SAMPLE, sample)
                }
            }
        }
    }
}