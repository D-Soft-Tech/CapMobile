package com.keystone.capmobile.di

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.keystone.capmobile.BuildConfig
import com.keystone.capmobile.R
import com.keystone.capmobile.SavedUser
import com.keystone.capmobile.data.api.ApiService
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.data.dataStore.ProtoDataSerializer
import com.keystone.capmobile.util.AppConstants
import com.keystone.capmobile.util.AppConstants.BASE_URL
import com.keystone.capmobile.util.AppConstants.PROTO_DATASTORE_FILE_NAME
import com.keystone.capmobile.util.ConstantsOnly.ACCOUNT_OPENING_BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    //    PROVISION OF RETROFIT STARTS HERE
    @Singleton
    @Provides
    @Named("defaultBU")
    fun providesDefaultBaseUrl() = BASE_URL

    @Singleton
    @Provides
    @Named("accountOBU")
    fun providesBaseUrl() = ACCOUNT_OPENING_BASE_URL

//    @Provides
//    @Singleton
//    fun providesOkHttpClient(): OkHttpClient {
//        val loggingInterceptor = HttpLoggingInterceptor()
//        loggingInterceptor.setLevel(
//            if (BuildConfig.DEBUG.not()) {
//                HttpLoggingInterceptor.Level.BODY
//            } else {
//                HttpLoggingInterceptor.Level.NONE
//            }
//        )
//        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .connectTimeout(30, TimeUnit.SECONDS)
//            .readTimeout(30, TimeUnit.SECONDS)
//            .writeTimeout(30, TimeUnit.SECONDS)
//            .build()
//    }
//
//    @Singleton
//    @Provides
//    fun providesRetrofit(okhttp: OkHttpClient, baseUrl: String): Retrofit =
//        Retrofit.Builder()
//            .addConverterFactory(GsonConverterFactory.create())
//            .baseUrl(baseUrl)
//            .client(okhttp)
//            .build()
//
//    @Singleton
//    @Provides
//    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

//    @Singleton
//    @Provides
//    fun providesOKHTTPClient() = if (BuildConfig.DEBUG) {
//        val loggingInterceptor = HttpLoggingInterceptor().apply {
//            setLevel(HttpLoggingInterceptor.Level.BODY)
//        }
//        OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
//            .build()
//    } else {
//        OkHttpClient.Builder()
//            .build()
//    }

    @Singleton
    @Provides
    fun providesOKHTTPClient(
        @ApplicationContext context: Context,
        hostSelectionInterceptor: BaseUrlSetterInterceptor
    ): OkHttpClient {
        val cacheSize = (5 * 1024 * 1024).toLong()
        val mCache = Cache(context.cacheDir, cacheSize)
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        return if (BuildConfig.DEBUG) {
            OkHttpClient().newBuilder()
                .cache(mCache)
                .retryOnConnectionFailure(true)
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .addInterceptor(loggingInterceptor)
                .addInterceptor(hostSelectionInterceptor)
                .followRedirects(true)
                .followSslRedirects(true)
                .build()
        } else {
            OkHttpClient().newBuilder()
                .connectTimeout(200, TimeUnit.SECONDS)
                .readTimeout(200, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .followRedirects(true)
                .followSslRedirects(true)
                .addInterceptor(hostSelectionInterceptor)
                .build()
        }
    }

    @Singleton
    @Provides
    @Named("defaultRet")
    fun providesRetrofit(okhttp: OkHttpClient, @Named("defaultBU") baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okhttp)
            .build()

    @Singleton
    @Provides
    @Named("openAccount")
    fun providesAccountOpeningRetrofit(okhttp: OkHttpClient, @Named("accountOBU") baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okhttp)
            .build()

    @Singleton
    @Provides
    @Named("default")
    fun providesApiService(@Named("defaultRet") retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)

    @Singleton
    @Provides
    @Named("accountOpening")
    fun providesAccountOpeningApiService(@Named("openAccount") retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun providesAppProtoDataStore(@ApplicationContext context: Context): AppProtoDataStore =
        AppProtoDataStore(context.dataStore)

    private val Context.dataStore: DataStore<SavedUser> by dataStore(
        fileName = PROTO_DATASTORE_FILE_NAME,
        serializer = ProtoDataSerializer
    )
}