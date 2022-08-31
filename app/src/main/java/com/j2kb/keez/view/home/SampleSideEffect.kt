package com.j2kb.keez.view.home

sealed class SampleSideEffect {
    data class Test(val test: String): SampleSideEffect()
}