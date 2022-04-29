package com.keystone.capmobile.data.model

import com.keystone.capmobile.util.currencyFormat

data class HardCoreDataObjects(
    var name: String,
    var accountNumber: String,
    var status: String,
    var product: Int,
    var balance: String? = "0"
) {
    fun getCurrencyFormattedBalance(): String = currencyFormat(balance?.toDouble() ?: 0.0)
}
