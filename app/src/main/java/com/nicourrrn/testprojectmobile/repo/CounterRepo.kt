package com.nicourrrn.testprojectmobile.repo

import com.nicourrrn.testprojectmobile.repo.dto.CounterResponse
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import javax.inject.Inject


object RequestInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        println("Request from ${request.url()}")
        return chain.proceed(request)
    }
}

object RetrofitClient {
    private const val BASE_URL = "http://192.168.0.104:8000/"
    val okHttpClient = OkHttpClient()
        .newBuilder()
        .addInterceptor(RequestInterceptor)
        .build()

    fun getClient(): Retrofit =
        Retrofit.Builder()
            .client(okHttpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
}

interface CounterApi {
    @GET("/")
    suspend fun getCounter(): CounterResponse

    @POST("/inc")
    suspend fun increment()
}


class CounterService @Inject constructor() {
    private val retrofit = RetrofitClient.getClient()
    private val counterApi = retrofit.create(CounterApi::class.java)

    suspend fun getCounter(): Int {
        val resp = counterApi.getCounter()
        return resp.counter
    }

    suspend fun incCounter() {
        counterApi.increment()
    }
}