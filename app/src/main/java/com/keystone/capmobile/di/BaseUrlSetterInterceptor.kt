package com.keystone.capmobile.di

import android.util.Log
import com.keystone.capmobile.util.ConstantsOnly.ACCOUNT_OPENING_BASE_URL
import com.keystone.capmobile.util.ConstantsOnly.BASE_URL_SELECTOR
import com.keystone.capmobile.util.ConstantsOnly.CAP_BASE_URL
import com.keystone.capmobile.util.DataStoreManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.net.URISyntaxException
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class BaseUrlSetterInterceptor @Inject constructor(
    private val dataStoreManager: DataStoreManager
) : Interceptor {

    @Volatile
    var hostUrl = CAP_BASE_URL.toHttpUrlOrNull()

    init {
        setHostBaseUrl()
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        var request: Request = chain.request()
        if (hostUrl != null) {
            setHostBaseUrl()
            Log.d("baseUrl", hostUrl.toString())
            Log.d("baseUrlAA", hostUrl!!.toUrl().toURI().host)
            var newUrl: HttpUrl? = null
            try {
                newUrl = request.url.newBuilder()
                    .scheme(hostUrl!!.scheme)
                    .host(hostUrl!!.toUrl().toURI().host)
                    .build()
                Log.d("baseUrl1", newUrl.toString())
            } catch (e: URISyntaxException) {
                e.printStackTrace()
            }
            assert(newUrl != null)
            request = newUrl?.let {
                Log.d("baseUrl2", newUrl.toString())
                request.newBuilder()
                    .url(it)
                    .build()
            }!!
        }
        return chain.proceed(request)
    }

    fun setHostBaseUrl() {
        CoroutineScope(Dispatchers.Default).launch {
            dataStoreManager.readBooleanFromDataStore(BASE_URL_SELECTOR).collect {
                Log.d("trueOrFalse", it.toString())
                hostUrl = if (it) {
                    ACCOUNT_OPENING_BASE_URL.toHttpUrlOrNull()
                } else {
                    CAP_BASE_URL.toHttpUrlOrNull()
                }
            }
        }
    }
}