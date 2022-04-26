package com.keystone.capmobile.data.model

data class Summary(
    val CASAClosingAvgBalance: Double,
    val CASAOpeningAvgBal: Double,
    val ClosingAvgBalance: Double,
    val DOMClosingAvgBalance: Int,
    val DOMOpeningAvgBal: Int,
    val DateGenerated: String,
    val FixedDepositClosingAvgBalance: Int,
    val FixedDepositOpeningAvgBal: Double,
    val OpeningAvgBal: Double? = 0.0,
    val account_officer: String,
    val grade: String,
    val name: String,
    val staffid: String
)