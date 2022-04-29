package com.keystone.capmobile.data.model

data class Summary(
    val CASAClosingAvgBalance: Double? = 0.0,
    val CASAOpeningAvgBal: Double? = 0.0,
    val ClosingAvgBalance: Double? = 0.0,
    val DOMClosingAvgBalance: Int? = 0,
    val DOMOpeningAvgBal: Int? = 0,
    val DateGenerated: String,
    val FixedDepositClosingAvgBalance: Int? = 0,
    val FixedDepositOpeningAvgBal: Double? = 0.0,
    var OpeningAvgBal: Double? = 0.0,
    val account_officer: String,
    val grade: String,
    val name: String,
    val staffid: String
)