package com.j2kb.keez.domain.usecase

import com.j2kb.keez.data.api.TestApiService
import com.j2kb.keez.data.api.model.SampleData
import javax.inject.Inject

class SampleUseCase @Inject constructor(private val testApiService: TestApiService) {
    suspend operator fun invoke(): SampleData {
        return testApiService.getTestValues().body() ?: SampleData(null, null, null)
    }
}