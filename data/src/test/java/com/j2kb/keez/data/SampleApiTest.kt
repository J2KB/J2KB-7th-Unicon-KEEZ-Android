package com.j2kb.keez.data

import com.j2kb.keez.data.api.SampleApiService
import com.j2kb.keez.data.api.model.SampleData
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SampleApiTest {

    private lateinit var sampleApiService: SampleApiService

    @Before
    fun setUp() {
        val netModule = NetModule()
        sampleApiService = netModule.provideRetrofit(
            netModule.provideGson(), netModule.provideOkhttpClient()
        ).create(SampleApiService::class.java)
    }

    @Test
    fun `Retrofit2 Get Real Test`() = runBlocking {
        val testResponse = sampleApiService.getTestValues()
        Assert.assertTrue(testResponse.isSuccessful)
        Assert.assertEquals(SampleData("id_1", "name_1", arrayListOf(1, 2, 3)), testResponse.body())
    }

    @Test
    fun `Retrofit2 Post Real Test`() = runBlocking {
        val testResponse = sampleApiService.postTestValues(SampleData("id_1", "name_1", arrayListOf(1, 2, 3)))
        Assert.assertTrue(testResponse.isSuccessful)
        Assert.assertEquals(SampleData("123", "postOkay", arrayListOf(3, 4, 5)), testResponse.body())
    }
}