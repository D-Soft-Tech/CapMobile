package com.keystone.capmobile.data.model

data class GeneralMessageResponseBody(
    val details: MessageDetails,
    val message: String,
    val status: String,
    val token: Boolean
)