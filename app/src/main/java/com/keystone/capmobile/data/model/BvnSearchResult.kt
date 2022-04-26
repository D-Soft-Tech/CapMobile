package com.keystone.capmobile.data.model

data class BvnSearchResult(
    val bvn: String,
    val dateOfBirth: String,
    val enrollmentBank: String,
    val enrollmentBranch: String,
    val firstName: String,
    val imageBase64: Any,
    val lastName: String,
    val middleName: String,
    val phoneNumber: String,
    val registrationDate: String
)