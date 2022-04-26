package com.keystone.capmobile.data.model

data class MyCabal(
    val ACCOUNT_NUMBER: String,
    val ACCOUNT_TITLE_1: String,
    val AverageBalance: Int,
    val BalanceLCY: Double,
    val CO_CODE: String,
    val Currency: String,
    val ExchangeRate: Int,
    val IsOnCards: Int,
    val IsonIbank: Int,
    val MaturityDate: Int,
    val MobileLimit: String,
    val OPENING_DATE: Int,
    val OpeningAvg: Int,
    val POSTING_RESTRICT: String,
    val POSTING_RESTRICT_REASON: String,
    val PROD_TYPE: String,
    val PhoneNumber: String,
    val STATUS: String,
    val account_officer: String,
    val isonmobile: Int,
    val isonussd: Int
) {
    fun mapToHardCore(): HardCoreDataObjects =
        HardCoreDataObjects(
        ACCOUNT_TITLE_1,
        ACCOUNT_NUMBER,
        STATUS,
        PROD_TYPE.toInt(),
        BalanceLCY.toString()
    )
}