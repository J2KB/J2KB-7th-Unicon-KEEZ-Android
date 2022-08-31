package com.j2kb.keez.view.markdown_test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.j2kb.keez.R

class SampleFragment : Fragment() {

    private lateinit var container: ViewGroup

    private var isCodeSelected = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sample, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        container = view.findViewById(R.id.container)
        isCodeSelected = savedInstanceState?.getBoolean(KEY_CODE_SELECTED) ?: false

        showCode()
    }

    private fun showCode() {
        isCodeSelected = true
        showFragment(TAG_CODE, TAG_PREVIEW) { SampleCodeFragment.init(sample) }
    }

    private fun showFragment(showTag: String, hideTag: String, provider: () -> Fragment) {
        val manager = childFragmentManager
        manager.beginTransaction().apply {
            setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)

            val existing = manager.findFragmentByTag(showTag)
            if (existing != null) {
                show(existing)
            } else {
                add(container.id, provider(), showTag)
            }

            manager.findFragmentByTag(hideTag)?.also {
                hide(it)
            }

            commitAllowingStateLoss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putBoolean(KEY_CODE_SELECTED, isCodeSelected)
    }

    private val sample: Sample by lazy(LazyThreadSafetyMode.NONE) {
        Sample(
            "io.noties.markwon.app.samples.CopyCodeBlockSample",
            "20210315112847",
            "Copy code block",
            "Copy contents of fenced code blocks",
            listOf(MarkwonArtifact.CORE),
            listOf(
                "block",
                "rendering",
                "span",
                "spanFactory"
            )
        )
    }

    companion object {
        private const val ARG_SAMPLE = "arg.Sample"
        private const val TAG_PREVIEW = "tag.Preview"
        private const val TAG_CODE = "tag.Code"
        private const val KEY_CODE_SELECTED = "key.Selected"

        fun init(sample: Sample): SampleFragment {
            val fragment = SampleFragment()
            fragment.arguments = Bundle().apply {
                putParcelable(ARG_SAMPLE, sample)
            }
            return fragment
        }
    }
}