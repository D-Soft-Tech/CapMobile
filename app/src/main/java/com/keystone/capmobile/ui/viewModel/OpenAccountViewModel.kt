package com.keystone.capmobile.ui.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keystone.capmobile.data.api.AccountOpeningRemoteApiServiceImplementation
import com.keystone.capmobile.data.dataStore.AppProtoDataStore
import com.keystone.capmobile.data.model.*
import com.keystone.capmobile.di.BaseUrlSetterInterceptor
import com.keystone.capmobile.util.ConstantsOnly.formatDateFromBvn
import com.keystone.capmobile.util.DataStoreManager
import com.keystone.capmobile.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class OpenAccountViewModel @Inject constructor(
    private val repository: AccountOpeningRemoteApiServiceImplementation,
    private val dataStore: DataStoreManager,
    private var protoDataStore: AppProtoDataStore,
    private val hostSelectionInterceptor: BaseUrlSetterInterceptor
) : ViewModel() {
    private var _bvnResponse: MutableLiveData<Resource<BvnValidationResponse>> = MutableLiveData()
    val bvnResponse: LiveData<Resource<BvnValidationResponse>> get() = _bvnResponse
    private var _bankBranchResponse: MutableLiveData<Resource<List<GetBankBranchResponse>>> =
        MutableLiveData()
    val bankBranchResponse: LiveData<Resource<List<GetBankBranchResponse>>> get() = _bankBranchResponse
    private var _selectedBankBranch: MutableLiveData<GetBankBranchResponse> = MutableLiveData()
    val selectedBankBranch: LiveData<GetBankBranchResponse> get() = _selectedBankBranch
    private var _wasUserSignatureSetViaDrawingTheSignature: MutableLiveData<Boolean> =
        MutableLiveData(false)
    val wasUserSignatureSetViaDrawingTheSignature: LiveData<Boolean> get() = _wasUserSignatureSetViaDrawingTheSignature
    private var _userAddress: MutableLiveData<UserAddress> = MutableLiveData()
    var _selectedProductCode: MutableLiveData<Int> = MutableLiveData()
    private var _userGender: MutableLiveData<String> = MutableLiveData()
    private var _userSignature: MutableLiveData<String> = MutableLiveData()
    val userSignature: LiveData<String> get() = _userSignature
    private var _userPassport: MutableLiveData<String> = MutableLiveData()
    private var _userIdCard: MutableLiveData<String> = MutableLiveData()
    private var _userUtilityBill: MutableLiveData<String> = MutableLiveData()
    private var _openAccountResponse: MutableLiveData<Resource<OpenAccountResponse>> =
        MutableLiveData()
    val openAccountResponse: LiveData<Resource<OpenAccountResponse>> get() = _openAccountResponse
    private var _reactivateAccountResponseBody: MutableLiveData<Resource<ReactivateAccountResponseBody>> =
        MutableLiveData()
    val reactivateAccountResponseBody: LiveData<Resource<ReactivateAccountResponseBody>> get() = _reactivateAccountResponseBody
    var _accountOfficerPhoneNumber: MutableLiveData<String> = MutableLiveData()
    // TODO_LATER //
    // Fetch products from the backend and save it in a livedata so that you can have

    fun openAccount() {
        bvnResponse.value?.data?.bvnSearchResult?.run {
            viewModelScope.launch {
                protoDataStore.readProto.collect { loggedInUser ->
                    val openAccountRequestBody = OpenAccountRequestBody(
                        "$lastName $firstName $middleName",
                        "${_userAddress.value?.preferredBranch}",
                        "${_bvnResponse.value?.data?.bvnSearchResult?.bvn}",
                        "Nigeria",
                        daocode = loggedInUser.daoCode,
                        dob = formatDateFromBvn(_bvnResponse.value?.data?.bvnSearchResult?.dateOfBirth!!),
                        email = _userAddress.value!!.userEmail,
                        firstName = firstName,
                        gender = _userGender.value!!,
                        null,
                        lastName = lastName,
                        location = _userAddress.value!!.residentialAddress,
                        "",
                        middleName = middleName,
                        "",
                        mobileNumber = phoneNumber,
                        passport = _userPassport.value!!,
                        "",
                        productCode = _selectedProductCode.value!!.toString(),
                        requestid = UUID.randomUUID().toString(),
                        residentialAddress = _userAddress.value!!.residentialAddress,
                        state = _userAddress.value!!.residentialState
                    )
                    repository.openAccount(openAccountRequestBody).collect { openAccountResponse ->
                        _openAccountResponse.value = openAccountResponse
                    }
                }
            }

        }
    }

    fun reactivateAccount(accountNumber: String) {
        // I just used all this strings because the only thing the backend actually requires is
        // the account number, this can be changed later in the future if the backend is updated to
        // actually check other fields apart from just the account number alone
        val reactivateAccRequestBody = ReactivateAccountRequestBody(
            accountNumber = accountNumber,
            bvn = "String",
            channelName = "String",
            customerId = "String",
            dob = "String",
            firstname = "String",
            lastname = "String",
            makerId = "String",
            phoneNumber = "String",
            requestBy = "String",
            requestChannel = "String",
            requestDate = "String",
            requestRef = "String",
            userName = "String"
        )
        viewModelScope.launch {
            repository.reactivateAccount(reactivateAccRequestBody).collect {
                _reactivateAccountResponseBody.value = it
            }
        }
    }

    fun validateBvn(userBvnRequestBody: BvnValidationRequestModel) {
        _bvnResponse.value = Resource.loading(null)
        viewModelScope.launch {
            repository.validateBvn(userBvnRequestBody).collect {
                _bvnResponse.value = it
            }
        }
    }

    fun setUserGender(userGender: String) {
        _userGender.value = userGender
    }

    fun getListOfBankBranch() {
        _bankBranchResponse.value = Resource.loading(null)
        viewModelScope.launch {
            repository.getListOfBankBranch().collect {
                _bankBranchResponse.value = it
            }
        }
    }

    fun setUserAddress(userAddress: UserAddress) {
        _userAddress.value = userAddress
    }

    fun setSelectedBankBranch(selectedBankBranch: GetBankBranchResponse) {
        _selectedBankBranch.value = selectedBankBranch
    }

    fun setUserSignature(userSignature: String, confirmSignatureByDrawing: Boolean) {
        _userSignature.value = userSignature
        _wasUserSignatureSetViaDrawingTheSignature.value = confirmSignatureByDrawing
    }

    fun setUserPassport(userPassport: String) {
        _userPassport.value = userPassport
    }

    fun setUserIdCard(userIdCard: String) {
        _userIdCard.value = userIdCard
    }

    fun setUserUtilityBill(userUtilityBill: String) {
        _userUtilityBill.value = userUtilityBill
    }
}