package com.example.capmobile.data.model

data class DaoChannelObject(
    var name: String,
    var phoneNumber: String,
    var mobile: Boolean,
    var ussd: Boolean,
    var iBank: Boolean,
    var cards: Boolean,
    var status: String
)