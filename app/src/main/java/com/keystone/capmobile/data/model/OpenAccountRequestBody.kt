package com.keystone.capmobile.data.model

data class OpenAccountRequestBody(
    val accountTitle: String,
    val branchCode: String,
    val bvn: String,
    val countryOfResidence: String,
    val daocode: String,
    val dob: Int,
    val email: String,
    val firstName: String,
    val gender: String,
    val identificationType: IdentificationType? = null,
    val lastName: String,
    val location: String,
    val maritalStatus: String,
    val middleName: String,
    val mnemonic: String,
    val mobileNumber: String,
    val passport: String,
    val processingMode: String,
    val productCode: String,
    val requestid: String,
    val residentialAddress: String,
    val state: String
)