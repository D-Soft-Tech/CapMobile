package com.example.capmobile.data.api

import com.example.capmobile.data.model.DaoChannelObject
import com.example.capmobile.data.model.LoginCredentials
import com.example.capmobile.data.model.LoginResponse
import com.example.capmobile.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject

class RemoteApiServiceImplementation @Inject constructor(
    private val remoteApiService: ApiService
) {

    suspend fun login(credentials: LoginCredentials): Flow<Resource<LoginResponse>> = flow {
        emit(Resource.loading(null))

        try {
            emit(
                Resource.success(
                    remoteApiService.login(credentials)
                )
            )
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(
                    Resource.timeOut(null)
                )
            } else {
                emit(
                    Resource.error(
                        null
                    )
                )
            }
        }
    }

    fun getAccounts(headers: Map<String, String>, transactionStatus: String): Flow<Resource<List<DaoChannelObject>>> = flow {
        emit(Resource.loading(null))

        try {
            val data = remoteApiService.getAccounts(headers, transactionStatus)

            emit(
                Resource.success(
                    data.fromDtoToEntity()
                )
            )

        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(
                    Resource.timeOut(null)
                )
            } else {
                emit(
                    Resource.error(
                        null
                    )
                )
            }
        }
    }

    fun getLevelsAndDescription(headers: Map<String, String>): Flow<Resource<List<String>>> = flow {
        emit(Resource.loading(null))

        try {
            val data = remoteApiService.getLevelsAndDescription(headers, "sub")

            emit(
                Resource.success(
                    data.fromDtoToEntity()
                )
            )

        } catch (e: Exception) {

            if (e == SocketTimeoutException()) {
                emit(
                    Resource.timeOut(null)
                )
            } else {
                emit(
                    Resource.error(
                        null
                    )
                )
            }
        }
    }
}