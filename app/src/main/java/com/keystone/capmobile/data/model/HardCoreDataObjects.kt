package com.keystone.capmobile.data.model

import com.keystone.capmobile.util.currencyFormat

data class HardCoreDataObjects(
    var name: String,
    var accountNumber: String,
    var status: String,
    var product: Int,
    var balance: String
) {
    fun getCurrencyFormattedBalance(): String = currencyFormat(balance.toDouble())
}
