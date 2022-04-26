package com.keystone.capmobile.data.model

data class AccountSummary(
    val details: SummaryDetails,
    val message: String,
    val status: String,
    val token: Boolean
)