package com.keystone.capmobile.data.api

import com.keystone.capmobile.data.model.*
import com.keystone.capmobile.di.BaseUrlSetterInterceptor
import com.keystone.capmobile.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.net.SocketTimeoutException
import javax.inject.Inject
import javax.inject.Named

class AccountOpeningRemoteApiServiceImplementation @Inject constructor(
    @Named("accountOpening") private val remoteApiService: ApiService,
    private val baseUrlSelector: BaseUrlSetterInterceptor
) {

    fun validateBvn(userBvnRequestBody: BvnValidationRequestModel): Flow<Resource<BvnValidationResponse>> = flow {
        emit(Resource.loading(null))

        try {
            baseUrlSelector.setHostBaseUrl()
            emit(Resource.success(remoteApiService.validateBvn(userBvnRequestBody)))
        } catch (e:Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun getListOfBankBranch(): Flow<Resource<List<GetBankBranchResponse>>> = flow {
        emit(Resource.loading(null))

        try {
            emit(Resource.success(remoteApiService.getListOfBranch()))
        } catch (e:Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun openAccount(openAccountRequest: OpenAccountRequestBody): Flow<Resource<OpenAccountResponse>> = flow {
        emit(Resource.loading(null))
        try {
            val data = remoteApiService.openAccount(openAccountRequest)
            emit(Resource.success(data))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }

    fun reactivateAccount(reactivateAccountRequestBody: ReactivateAccountRequestBody): Flow<Resource<ReactivateAccountResponseBody>> = flow {
        emit(Resource.loading(null))
        try {
            val data = remoteApiService.reactivateAccount(reactivateAccountRequestBody)
            emit(Resource.success(data))
        } catch (e: Exception) {
            if (e == SocketTimeoutException()) {
                emit(Resource.timeOut(null))
            } else {
                emit(Resource.error(null))
            }
        }
    }
}