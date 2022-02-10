package com.example.capmobile.data.model

data class LoginResponse(
    val details: Details,
    val message: String,
    val status: String,
    val token: Boolean
)