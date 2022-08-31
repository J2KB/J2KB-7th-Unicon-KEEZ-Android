package com.j2kb.keez.view.markdown_test

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Sample(
        val javaClassName: String,
        val id: String,
        val title: String,
        val description: String,
        val artifacts: List<MarkwonArtifact>,
        val tags: List<String>
) : Parcelable {

    enum class Language {
        JAVA, KOTLIN, SWIFT
    }

    data class Code(val language: Language, val sourceCode: String)
}