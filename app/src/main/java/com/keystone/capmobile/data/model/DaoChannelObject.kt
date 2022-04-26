package com.keystone.capmobile.data.model

data class DaoChannelObject(
    var name: String,
    var number: String,
    var emailAddress: String,
    var mobile: Boolean,
    var ussd: Boolean,
    var iBank: Boolean,
    var cards: Boolean,
    var status: String
)