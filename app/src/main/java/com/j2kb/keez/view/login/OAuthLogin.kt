package com.j2kb.keez.view.login

interface OAuthLogin {
    fun sendToken(token: String)
    fun sendException(message: String)
}