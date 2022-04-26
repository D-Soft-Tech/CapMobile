package com.keystone.capmobile.data.model

data class DetailsX(
    val lastSixMonths: List<LastSixMonth>,
    val myCabal: List<MyCabal>,
    val summary: Summary
) {
    fun mapToHardCoreDataObjects(): List<HardCoreDataObjects> =
        myCabal.map { it.mapToHardCore() }
}