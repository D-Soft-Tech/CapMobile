package com.keystone.capmobile.data.model

data class MyCabal(
    val ACCOUNT_NUMBER: String,
    val ACCOUNT_TITLE_1: String,
    val AverageBalance: Int? = 0,
    val BalanceLCY: Double? = 0.0,
    val CO_CODE: String,
    val Currency: String,
    val ExchangeRate: Int? = 0,
    val IsOnCards: Int? = 0,
    val IsonIbank: Int? = 0,
    val MaturityDate: Int? = 0,
    val MobileLimit: String,
    val OPENING_DATE: Int? = 0,
    val OpeningAvg: Int? = 0,
    val POSTING_RESTRICT: String,
    val POSTING_RESTRICT_REASON: String,
    val PROD_TYPE: String,
    val PhoneNumber: String,
    val STATUS: String,
    val account_officer: String,
    val isonmobile: Int? = 0,
    val isonussd: Int? = 0
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