package com.keystone.capmobile.data.model

data class ReactivateAccountRequestBody(
    val accountNumber: String,
    val bvn: String,
    val channelName: String,
    val customerId: String,
    val dob: String,
    val firstname: String,
    val lastname: String,
    val makerId: String,
    val phoneNumber: String,
    val requestBy: String,
    val requestChannel: String,
    val requestDate: String,
    val requestRef: String,
    val userName: String
)