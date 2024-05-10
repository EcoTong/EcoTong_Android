package id.ac.istts.ecotong.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
//        val token = runBlocking { dataStoreManager.storyJwtToken.first() }
//        if (token.isNullOrEmpty()) {
//            return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
//        }
//        val authInterceptor = Interceptor { chain ->
//            val req = chain.request()
//            val requestHeaders =
//                req.newBuilder().addHeader("Authorization", "Bearer $token").build()
//            chain.proceed(requestHeaders)
//        }
//        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
//            .addInterceptor(authInterceptor).build()
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
    }

    @Provides
    fun provideEcotongApi(client: OkHttpClient): EcotongApiService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(EcotongApiService::class.java)
    }
}