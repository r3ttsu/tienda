package com.example.tienda.network

import com.example.tienda.helper.Constant
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private val interceptor = HttpLoggingInterceptor()
    private val setInterceptor: HttpLoggingInterceptor
        get() {
//            return if (BuildConfig.DEBUG) {
//                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
//            } else {
//                interceptor.setLevel(HttpLoggingInterceptor.Level.NONE)
//            }
            return interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        }

    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(setInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .client(client)
        .baseUrl(Constant.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitAPI: RetrofitAPI = retrofit.create(RetrofitAPI::class.java)
}