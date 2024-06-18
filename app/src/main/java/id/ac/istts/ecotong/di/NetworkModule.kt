package id.ac.istts.ecotong.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import id.ac.istts.ecotong.BuildConfig
import id.ac.istts.ecotong.data.local.datastore.SessionManager
import id.ac.istts.ecotong.data.remote.service.EcotongApiService
import id.ac.istts.ecotong.data.remote.service.EcotongMLService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideOkHttpClient(sessionManager: SessionManager): OkHttpClient {
        val loggingInterceptor = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        } else {
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
        }
        val token = runBlocking { sessionManager.ecotongJwtToken.first() }
        if (token.isNullOrEmpty()) {
            return OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        }
        val authInterceptor = Interceptor { chain ->
            val req = chain.request()
            val requestHeaders = req.newBuilder().addHeader("authorization", token).build()
            chain.proceed(requestHeaders)
        }
        return OkHttpClient.Builder().addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor).build()
    }

    @Provides
    fun provideEcotongApi(client: OkHttpClient): EcotongApiService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BuildConfig.API_BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(EcotongApiService::class.java)
    }
    @Provides
    @Singleton
    fun provideEcotongMLApi(client: OkHttpClient): EcotongMLService {
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        return Retrofit.Builder().baseUrl(BuildConfig.ML_BASE_URL).client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi)).build()
            .create(EcotongMLService::class.java)
    }
}