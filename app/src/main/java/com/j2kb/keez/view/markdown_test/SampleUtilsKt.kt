package com.j2kb.keez.view.markdown_test

import android.content.Context
import java.io.InputStream
import java.io.IOException
import java.util.Scanner

val MarkwonArtifact.displayName: String
    get() = "@${artifactName()}"

val String.tagDisplayName: String
    get() = "#$this"

private const val SAMPLE_PREFIX = "io.noties.markwon.app."

fun Sample.readCode(context: Context): Sample.Code {
    val assets = context.assets

    // keep sample and nested directories
    val path = javaClassName
            .removePrefix(SAMPLE_PREFIX)
            .replace('.', '/')

    fun obtain(path: String): InputStream? {
        return try {
            assets.open(path)
        } catch (t: Throwable) {
            null
        }
    }

    // now, we have 2 possibilities -> Kotlin or Java
    var language: Sample.Language = Sample.Language.KOTLIN
    var stream = obtain("$path.kt")
    if (stream == null) {
        language = Sample.Language.JAVA
        stream = obtain("$path.java")
    }

    if (stream == null) {
        throw IllegalStateException("Cannot obtain sample file at path: $path")
    }

    val code = stream.readStringAndClose()

    return Sample.Code(language, code)
}

fun loadReadMe(context: Context): String {
    val stream = context.assets.open("README.md")
    return stream.readStringAndClose()
}


fun InputStream.readStringAndClose(): String {
    try {
        val scanner = Scanner(this).useDelimiter("\\A")
        if (scanner.hasNext()) {
            return scanner.next()
        }
        return ""
    } finally {
        try {
            close()
        } catch (e: IOException) {
            // ignored
        }
    }
}