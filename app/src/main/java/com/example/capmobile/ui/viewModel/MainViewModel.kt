package com.example.capmobile.ui.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capmobile.data.model.DaoChannelObject
import com.example.capmobile.data.model.LoginCredentials
import com.example.capmobile.data.model.LoginResponseObserver
import com.example.capmobile.data.repository.Repository
import com.example.capmobile.util.ConstantsOnly.USER_FIRST_NAME
import com.example.capmobile.util.DataStoreManager
import com.example.capmobile.util.Resource
import com.example.capmobile.util.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    private val dataStore: DataStoreManager
): ViewModel(){
    private var _loginResponse: MutableLiveData<LoginResponseObserver> = MutableLiveData()
    val loginResponse: LiveData<LoginResponseObserver> get() = _loginResponse

    // Get Dao Accounts
    private var _getAccountResponse: MutableLiveData<Resource<List<DaoChannelObject>>> = MutableLiveData()
    val getAccountResponse: LiveData<Resource<List<DaoChannelObject>>> get() = _getAccountResponse

    // Get Dao Accounts
    private var _getLevelsAndDescription: MutableLiveData<Resource<List<String>>> = MutableLiveData()
    val getLevelsAndDescription: LiveData<Resource<List<String>>> get() = _getLevelsAndDescription

    fun login(credentials: LoginCredentials) {
        _loginResponse.value = LoginResponseObserver(Status.LOADING, "Loading...")

        viewModelScope.launch {
            repository.remoteApiServiceImplementation.login(credentials).collect {
                Log.d("loginResponse", it.message)
                when(it.status) {
                    Status.SUCCESS -> {
                        _loginResponse.value = LoginResponseObserver(it.status, it.message)
                        dataStore.saveToken(it.data!!.details.token)
                        dataStore.saveStringToDataStore(it.data.details.name, USER_FIRST_NAME)
                    }
                    Status.LOADING -> {
                        _loginResponse.value = LoginResponseObserver(it.status, it.message)
                    }
                    Status.ERROR -> {
                        _loginResponse.value = LoginResponseObserver(it.status, it.message)
                    }
                    Status.TIMEOUT -> {
                        _loginResponse.value = LoginResponseObserver(it.status, it.message)
                    }
                }
            }
        }
    }

    private fun getToken() = dataStore.getToken()

    fun getAccounts(transactionStatus: String) {
        viewModelScope.launch {
            getToken().collect {
                val headers = HashMap<String, String>()
                headers["x-access-token"] = it
                headers["Authorization"] = it
                headers["x-auth-token"] = it
                repository.remoteApiServiceImplementation.getAccounts(headers, transactionStatus).collect { resource ->
                    _getAccountResponse.value = resource
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
                repository.remoteApiServiceImplementation.getLevelsAndDescription(headers).collect { resource ->
                    _getLevelsAndDescription.value = resource
                }
            }
        }
    }
}