package com.keystone.capmobile.data.api

import android.util.Log
import com.keystone.capmobile.data.model.*
import com.keystone.capmobile.di.BaseUrlSetterInterceptor
import com.keystone.capmobile.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class RemoteApiServiceImplementation @Inject constructor(
    @Named("default") private val remoteApiService: ApiService,
    private val baseUrlSelector: BaseUrlSetterInterceptor
) {
    suspend fun login(credentials: LoginCredentials): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(remoteApiService.login(credentials)))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun getAccounts(
        headers: Map<String, String>,
        transactionStatus: String
    ): Flow<Resource<List<DaoChannelObject>>> = flow {
        emit(Resource.loading(null))
        try {
            val data = remoteApiService.getAccounts(headers, transactionStatus)
            Log.d("datadata", data.toString())
            emit(Resource.success(data.fromDtoToEntity()))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun getLevelsAndDescription(headers: Map<String, String>): Flow<Resource<List<Data>>> = flow {
        emit(Resource.loading(null))
        try {
            val data = remoteApiService.getLevelsAndDescription(headers, "sub")
            emit(Resource.success(data.fromDtoToEntity()))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun fetchMessage(
        headers: Map<String, String>,
        levelsAndStatus: Map<String, String>
    ): Flow<Resource<GeneralMessageResponseBody>> = flow {
        emit(Resource.loading(null))
        try {
            val data = remoteApiService.fetchMessage(headers, levelsAndStatus)
            emit(Resource.success(data))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun sendMessage(
        headers: Map<String, String>,
        messageRequestBody: SendMessageRequestBody
    ): Flow<Resource<GeneralMessageResponseBody>> = flow {
        emit(Resource.loading(null))
        try {
            emit(Resource.success(remoteApiService.sendMessage(headers, messageRequestBody)))
        } catch (e: java.lang.Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun getHardCore(
        headers: Map<String, String>,
        hardCoreRequestBody: GetHardCoreRequestBody
    ): Flow<Resource<GetHardCoreResponseBody>> = flow {
        emit(Resource.loading(null))
        try {
          val hardCore =  remoteApiService.getHardCore(headers, hardCoreRequestBody)
            emit(Resource.success(hardCore))
        } catch (e: Exception) {
            println(e)
            Log.d("1e==", e.localizedMessage)
            if (e != SocketTimeoutException()) {
                Log.d("1e==", e.localizedMessage)
                emit(Resource.error(null))
            } else {
                Log.d("2e==", e.localizedMessage)
                emit(Resource.timeOut(null))
            }
        }
    }

    fun getAccountSummary(
        headers: Map<String, String>,
    ): Flow<Resource<AccountSummary>> = flow {
        emit(Resource.loading(null))
        try {
            val summary = remoteApiService.getAccountSummary(headers)
            emit(Resource.success(summary))
        } catch (e: java.lang.Exception) {
            if (e != SocketTimeoutException()) {
                emit(Resource.error(null))
            } else {
                emit(Resource.timeOut(null))
            }
        }
    }
}