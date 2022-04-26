package com.keystone.capmobile.data.model

import com.keystone.capmobile.util.roundToDecimalPlace

data class SummaryDetails(
    val dashboard: Dashboard,
    val summary: List<SummaryBreakDown>
) {

    fun getPercentageInactiveAcc(): Double =
        (100.00 - summary[0].PctActive).roundToDecimalPlace(2)

    fun getPercentageTotalAcc(): Double =
        100.toDouble()
}