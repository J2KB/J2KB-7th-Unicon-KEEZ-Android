package com.j2kb.keez.data.api.model

data class SampleData(
    val id: String? = null,
    val name: String? = null,
    val values: ArrayList<Int>? = null
) {
    fun isNotNull(): Boolean {
        return id != null && name != null && values != null
    }
}