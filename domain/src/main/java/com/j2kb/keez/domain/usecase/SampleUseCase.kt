package com.j2kb.keez.domain.usecase

import com.j2kb.keez.data.api.SampleApiService
import com.j2kb.keez.data.api.model.SampleData
import javax.inject.Inject

class SampleUseCase @Inject constructor(private val testApiService: SampleApiService) {
    suspend operator fun invoke(): SampleData {
        return testApiService.getTestValues().body() ?: SampleData(null, null, null)
    }
}