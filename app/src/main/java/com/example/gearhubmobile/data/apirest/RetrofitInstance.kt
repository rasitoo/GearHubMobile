package com.example.gearhubmobile.data.apirest

import androidx.compose.ui.semantics.Role
import com.example.gearhubmobile.utils.AuthInterceptor
import com.example.gearhubmobile.utils.SessionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

/**
 * @author Rodrigo
 * @date 21 mayo, 2025
 */
@Module
@InstallIn(SingletonComponent::class)
object RetrofitInstance {

    private const val BASE_URL = "http://vms.iesluisvives.org:25003/"

    @Provides
    @Singleton
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideAuthInterceptor(sessionManager: SessionManager): AuthInterceptor {
        return AuthInterceptor(sessionManager)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        logging: HttpLoggingInterceptor,
        authInterceptor: AuthInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(logging)
            .addInterceptor(authInterceptor)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Provides
    @Singleton
    fun provideCommunityApi(retrofit: Retrofit): CommunityApi{
        return retrofit.create(CommunityApi::class.java)
    }
    @Provides
    @Singleton
    fun provideAuthApi(retrofit: Retrofit): AuthApi  {
        return retrofit.create(AuthApi::class.java)
    }
    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): ChatApi {
        return retrofit.create(ChatApi::class.java)
    }
    @Provides
    @Singleton
    fun provideProfileApi(retrofit: Retrofit): ProfileApi {
        return retrofit.create(ProfileApi::class.java)
    }
    @Provides
    @Singleton
    fun provideFollowsApi(retrofit: Retrofit): FollowsApi {
        return retrofit.create(FollowsApi::class.java)
    }
    @Provides
    @Singleton
    fun provideMessageApi(retrofit: Retrofit): MessageApi {
        return retrofit.create(MessageApi::class.java)
    }
    @Provides
    @Singleton
    fun provideResponseApi(retrofit: Retrofit): ResponseApi {
        return retrofit.create(ResponseApi::class.java)
    }
    @Provides
    @Singleton
    fun provideRolesApi(retrofit: Retrofit): RolesApi {
        return retrofit.create(RolesApi::class.java)
    }
    @Provides
    @Singleton
    fun provideThreadApi(retrofit: Retrofit): ThreadApi {
        return retrofit.create(ThreadApi::class.java)
    }
    @Provides
    @Singleton
    fun provideUserChatApi(retrofit: Retrofit): UserChatApi {
        return retrofit.create(UserChatApi::class.java)
    }
    @Provides
    @Singleton
    fun provideReviewApi(retrofit: Retrofit): ReviewApi {
        return retrofit.create(ReviewApi::class.java)
    }
}