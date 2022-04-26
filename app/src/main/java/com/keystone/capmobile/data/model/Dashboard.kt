package com.keystone.capmobile.data.model

import com.keystone.capmobile.util.roundToDecimalPlace

data class Dashboard(
    val totalAccounts: Int,
    val totalActive: Int,
    val totalActiveCards: Int,
    val totalActiveIbank: Int,
    val totalActiveMobile: Int,
    val totalActiveUSSD: Int,
    val totalDormant: Int,
    val totalFunded: Int,
    val totalIanctiveCards: Int,
    val totalInactive: Int,
    val totalInactiveIbank: Int,
    val totalInactiveMobile: Int,
    val totalInactiveUSSD: Int,
    val totalNotFunded: Int,
    val totalUnfunded: Int
) {
    fun getPercentageDormant(): Double =
        ((totalDormant.toDouble() / totalAccounts.toDouble()) * 100).roundToDecimalPlace(2)
}