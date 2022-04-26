package com.keystone.capmobile.data.model

data class BvnValidationRequestModel(
    val bvn: String = "SYSTEM",
    val channelname: String = "Mobile",
    val username: String
)