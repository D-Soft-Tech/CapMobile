package com.keystone.capmobile.data.model

data class LastSixMonth(
    val ActualBalance: Double? = 0.0,
    val AvgMovement: Double? = 0.0,
    val ClosingAvg: Double? = 0.0,
    val LastWorkindate: String,
    val Month: String,
    val OpeningAvgBal: Double? = 0.0,
    val account_officer: String,
    val grade: String,
    val name: String,
    val role: String,
    val staffid: String
)