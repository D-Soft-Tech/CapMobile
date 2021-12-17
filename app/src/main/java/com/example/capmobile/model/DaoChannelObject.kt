package com.example.capmobile.model

data class DaoChannelObject(
    var name: String,
    var mobile: Boolean,
    var ussd: Boolean,
    var iBank: Boolean,
    var cards: Boolean,
    var status: String
)