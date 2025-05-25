package com.example.gearhubmobile.data.apirest

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */

object RetrofitInstance {

    private const val BASE_URL = "http://localhost:8000/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val communityApi: CommunityApi by lazy {
        retrofit.create(CommunityApi::class.java)
    }
    val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    val chatApi: ChatApi by lazy {
        retrofit.create(ChatApi::class.java)
    }
    val profileApi: ProfileApi by lazy {
        retrofit.create(ProfileApi::class.java)
    }
    val reviewApi: ReviewApi by lazy {
        retrofit.create(ReviewApi::class.java)
    }

}
