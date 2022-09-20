package com.j2kb.keez.data

import android.app.Application
import android.util.Log
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.j2kb.keez.data.api.TestApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.net.SocketTimeoutException
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetModule {

    @Provides
    @Singleton
    fun provideHttpCache(application: Application): Cache {
        val cacheSize = 10L * 1024 * 1024
        return Cache(application.cacheDir, cacheSize)
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .create()
    }

    @Provides
    @Singleton
    fun provideOkhttpClient(cache: Cache): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(Interceptor {
                return@Interceptor onOnIntercept(it)
            })
            .build()
    }

    @Throws(IOException::class)
    private fun onOnIntercept(chain: Interceptor.Chain): Response {
        try {
            val response: Response = chain.proceed(chain.request())
            val content = response.body()?.string() ?: ""
            Log.d("OkHttp", content)
            return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), content)).build()
        } catch (exception: SocketTimeoutException) {
            exception.printStackTrace()
        }
        return chain.proceed(chain.request())
    }

    @Provides
    @Singleton
    fun provideRetrofit(gson: Gson, okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideTestApiService(retrofit: Retrofit): TestApiService {
        return retrofit.create(TestApiService::class.java)
    }

    companion object {
        private const val BASE_URL: String = "http://192.168.35.3:8080"
    }
}