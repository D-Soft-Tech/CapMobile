package com.keystone.capmobile.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.data.model.*
import com.keystone.capmobile.data.repository.Repository
import com.keystone.capmobile.di.BaseUrlSetterInterceptor
import com.keystone.capmobile.util.DataStoreManager
import com.keystone.capmobile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStoreManager,
    private val protoDataStore: AppProtoDataStore,
    private val hostSelectionInterceptor: BaseUrlSetterInterceptor
) : ViewModel() {
    private var _loginResponse: MutableLiveData<Resource<LoginResponse>> = MutableLiveData()
    val loginResponse: LiveData<Resource<LoginResponse>> get() = _loginResponse
    private var _getAccountResponse: MutableLiveData<Resource<List<DaoChannelObject>>> =
        MutableLiveData()
    val getAccountResponse: LiveData<Resource<List<DaoChannelObject>>> get() = _getAccountResponse
    private var _getAccountSummary: MutableLiveData<Resource<AccountSummary>> =
        MutableLiveData()
    val getAccountSummary: LiveData<Resource<AccountSummary>> get() = _getAccountSummary
    private var _getLevelsAndDescription: MutableLiveData<Resource<List<Data>>> = MutableLiveData()
    val getLevelsAndDescription: LiveData<Resource<List<Data>>> get() = _getLevelsAndDescription
    private var _messageFetchedFromTheBackEnd: MutableLiveData<Resource<GeneralMessageResponseBody>> =
        MutableLiveData()
    val messageFetchedFromTheBackEnd: LiveData<Resource<GeneralMessageResponseBody>> get() = _messageFetchedFromTheBackEnd
    private var _sendMessageResponse: MutableLiveData<Resource<SendMessageResponseBody>> =
        MutableLiveData()
    val sendMessageResponse: LiveData<Resource<SendMessageResponseBody>> get() = _sendMessageResponse
    private var _getHardCore: MutableLiveData<Resource<GetHardCoreResponseBody>> =
        MutableLiveData()
    val getHardCore: LiveData<Resource<GetHardCoreResponseBody>> get() = _getHardCore

    init {
        getAccounts("ALL")
    }

    fun login(credentials: LoginCredentials) {
        viewModelScope.launch {
            repository.remoteApiServiceImplementation.login(credentials).collect {
                _loginResponse.value = it
            }
        }
    }

    private fun getToken() = dataStore.getToken()

    fun getAccounts(transactionStatus: String) {
        viewModelScope.launch {
            getToken().collect {
                Log.d("tokennnnnnns", it)
                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["Authorization"] = it
                headers["x-auth-token"] = it
                repository.remoteApiServiceImplementation.getAccounts(headers, transactionStatus)
                    .collect { result ->
                        _getAccountResponse.value = result
                        Log.d("AAAAAAAA", result.data.toString())
                    }
            }
        }
    }

    fun getLevelsAndDescription() {
        viewModelScope.launch {
            getToken().collect {
                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["Authorization"] = it
                headers["x-auth-token"] = it
                repository.remoteApiServiceImplementation.getLevelsAndDescription(headers)
                    .collect { resource ->
                        Log.d("tokkkkeeeen", it)
                        Log.d("leevvv", resource.data.toString())
                        _getLevelsAndDescription.value = resource
                    }
            }
        }
    }

    @ExperimentalCoroutinesApi
    fun fetchMessage(level: String, transactionStatus: String) {
        viewModelScope.launch {
            getToken().collect {
                val queries = HashMap<String, String>()
                queries["level"] = level
                queries["transacting"] = transactionStatus

                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["Authorization"] = it
                headers["x-auth-token"] = it
                repository.remoteApiServiceImplementation.fetchMessage(headers, queries)
                    .collect { resource ->
                        Log.d("FFLLWW", resource.data.toString())
                        _messageFetchedFromTheBackEnd.value = resource
                    }
            }
        }
    }

    fun sendMessage(sendMessageRequestBody: SendMessageRequestBody) {
        viewModelScope.launch {
            getToken().collect {
                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["x-auth-token"] = it
                headers["Authorization"] = it

                repository.remoteApiServiceImplementation.sendMessage(
                    headers,
                    sendMessageRequestBody
                ).collect { sendMessageResponse ->
                    Log.d("MESSAGE_RESPONSE_1", sendMessageResponse.toString())
                    _sendMessageResponse.value = sendMessageResponse
                }
            }
        }
    }

    fun getHardCore(selectedDate: String) {
        viewModelScope.launch {
            protoDataStore.readProto.collect { savedUser ->
                getToken().collect {
                    val headers = HashMap<String, String>()
                    headers["x-access-token"] = it
                    headers["x-auth-token"] = it
                    headers["x-Authorization-token"] = it

                    repository.remoteApiServiceImplementation.getHardCore(
                        headers,
                        GetHardCoreRequestBody(savedUser.daoCode, selectedDate)
                    ).collect { hardCordReqResponse ->
                        _getHardCore.value = hardCordReqResponse
                    }
                }
            }
        }
    }

    fun getAccountSummary() {
        viewModelScope.launch {
            getToken().collect {
                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["x-auth-token"] = it
                headers["x-Authorization-token"] = it

                repository.remoteApiServiceImplementation.getAccountSummary(
                    headers
                ).collect { hardCordReqResponse ->
                    _getAccountSummary.value = hardCordReqResponse
                }
            }
        }
    }
}