package com.example.capmobile.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import com.example.capmobile.BuildConfig
import com.example.capmobile.SavedUser
import com.example.capmobile.data.api.ApiService
import com.example.capmobile.data.dataStore.AppProtoDataStore
import com.example.capmobile.data.dataStore.ProtoDataSerializer
import com.example.capmobile.util.AppConstants.BASE_URL
import com.example.capmobile.util.AppConstants.PROTO_DATASTORE_FILE_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LoginModule {
    //    PROVISION OF RETROFIT STARTS HERE
    @Singleton
    @Provides
    fun providesBaseUrl() = BASE_URL

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

    @Singleton
    @Provides
    fun providesOKHTTPClient() = if (BuildConfig.DEBUG) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    } else {
        OkHttpClient.Builder()
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofit(okhttp: OkHttpClient, baseUrl: String): Retrofit =
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(baseUrl)
            .client(okhttp)
            .build()

    @Singleton
    @Provides
    fun providesApiService(retrofit: Retrofit): ApiService = retrofit.create(ApiService::class.java)


    @Singleton
    @Provides
    fun providesAppProtoDataStore(@ApplicationContext context: Context): AppProtoDataStore =
        AppProtoDataStore(context.dataStore)

    private val Context.dataStore: DataStore<SavedUser> by dataStore(
        fileName = PROTO_DATASTORE_FILE_NAME,
        serializer = ProtoDataSerializer
    )

    // PROVISION OF ROOM DATABASE STARTS HERE
//    @Singleton
//    @Provides
//    fun providesLocalDB(@ApplicationContext appContext: Context): LocalDataBase =
//        Room.databaseBuilder(
//            appContext,
//            LocalDataBase::class.java,
//            "MaxNG"
//        ).build()
//
//    @Singleton
//    @Provides
//    fun providesLocalDao(localDB: LocalDataBase): LocalDao =
//        localDB.getLocalDao()
}