package com.j2kb.keez.auth

data class LoginResult(
    var token: String = "",
    var isOk: Boolean = false,
    var message: String = ""
)