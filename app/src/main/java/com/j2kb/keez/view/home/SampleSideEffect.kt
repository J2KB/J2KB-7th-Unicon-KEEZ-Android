package com.j2kb.keez.view.home

sealed class SampleSideEffect {
    data class Toast(val test: String): SampleSideEffect()
}