package com.keystone.capmobile.data.model

data class SendMessageRequestBody(
    val level: String,
    val message: String,
    val subject: String,
    val transacting: String
)