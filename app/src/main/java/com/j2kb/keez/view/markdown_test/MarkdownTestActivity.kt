package com.j2kb.keez.view.markdown_test

import android.os.Bundle
import android.view.Window
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.j2kb.keez.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MarkdownTestActivity : FragmentActivity() {

    private val viewModel: TestViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sample_text_view)

        if (supportFragmentManager.findFragmentById(Window.ID_ANDROID_CONTENT) == null) {

            supportFragmentManager.beginTransaction()
                .add(Window.ID_ANDROID_CONTENT, SampleFragment())
                .addToBackStack(null)
                .commit()

//            // process deeplink if we are not restored
//            val deeplink = Deeplink.parse(intent.data)
//
//            val deepLinkFragment: Fragment? = if (deeplink != null) {
//                when (deeplink) {
//                    is Deeplink.Sample -> App.sampleManager.sample(deeplink.id)
//                        ?.let { SampleFragment.init(it) }
//                    is Deeplink.Search -> SampleListFragment.init(deeplink.search)
//                }
//            } else null
//
//            if (deepLinkFragment != null) {
//                supportFragmentManager.beginTransaction()
//                    .replace(Window.ID_ANDROID_CONTENT, deepLinkFragment)
//                    .addToBackStack(null)
//                    .commit()
//            }
        }
    }
}