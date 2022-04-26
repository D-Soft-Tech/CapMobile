package com.keystone.capmobile.data.model

data class SummaryBreakDown(
    val ACTIVE: Int,
    val ACTIVEONCARDS: Int,
    val ACTIVEONIBANK: Int,
    val ACTIVEONMOBILE: Int,
    val ACTIVEONUSSD: Int,
    val AccountOfficer: String,
    val BRANCH_NAME: String,
    val DAOCode: String,
    val DIRECTORATE: String,
    val DIVISION: String,
    val DORMANT: Int,
    val FundedForCardCount: Int,
    val INACTIVE: Int,
    val NOTACTIVEONCARDS: Int,
    val NOTACTIVEONIBANK: Int,
    val NOTACTIVEONMOBILE: Int,
    val NOTACTIVEONUSSD: Int,
    val NotFundedForCardCount: Int,
    val PctActive: Double,
    val PctCard: Double,
    val PctIbank: Double,
    val PctMobile: Double,
    val Pctussd: Double,
    val TotalAccount: Int,
    val TotalAllAccount: Int,
    val UnFundedCount: Int,
    val branch_code: String,
    val region: String
)