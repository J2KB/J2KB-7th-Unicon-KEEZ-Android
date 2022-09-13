package com.j2kb.keez.domain

import com.j2kb.keez.data.api.SampleApiService
import com.j2kb.keez.domain.usecase.SampleUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    @Singleton
    fun provideSampleData(testApiService: SampleApiService): SampleUseCase {
        return SampleUseCase(testApiService)
    }
}