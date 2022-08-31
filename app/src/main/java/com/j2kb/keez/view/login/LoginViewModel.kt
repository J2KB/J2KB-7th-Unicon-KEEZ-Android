package com.j2kb.keez.view.login

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.j2kb.keez.view.home.MainActivity
import javax.inject.Inject

class LoginViewModel @Inject constructor(): ViewModel() {
    fun moveTo(activity: LoginActivity, targetActivity: Class<*>) {
        activity.startActivity(Intent(activity, targetActivity))
    }

    fun getTestMarkdown(): String {
        TODO("Not yet implemented")
    }
}