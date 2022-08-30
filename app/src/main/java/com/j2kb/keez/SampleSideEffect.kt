package com.j2kb.keez

sealed class SampleSideEffect {
    data class Test(val test: String): SampleSideEffect()
}