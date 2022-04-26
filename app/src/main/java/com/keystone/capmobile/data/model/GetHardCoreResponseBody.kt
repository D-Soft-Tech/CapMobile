package com.keystone.capmobile.data.model

data class GetHardCoreResponseBody(
    val details: DetailsX,
    val message: String,
    val status: String,
    val token: Boolean
)