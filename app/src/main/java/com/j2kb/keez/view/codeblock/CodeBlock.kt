package com.j2kb.keez.view.codeblock

object CodeBlock {
    enum class Language {
        SWIFT
    }

    data class Code(val language: Language, val sourceCode: String)
}