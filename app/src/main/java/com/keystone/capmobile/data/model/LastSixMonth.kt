package com.keystone.capmobile.data.model

data class LastSixMonth(
    val ActualBalance: Double,
    val AvgMovement: Double,
    val ClosingAvg: Double,
    val LastWorkindate: String,
    val Month: String,
    val OpeningAvgBal: Double,
    val account_officer: String,
    val grade: String,
    val name: String,
    val role: String,
    val staffid: String
)